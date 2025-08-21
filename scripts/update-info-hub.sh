#!/bin/bash

# Info-Hub Automated Update Script
# Implements the update protocol defined in info-hub.md

set -e

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Configuration
PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
INFO_HUB="$PROJECT_ROOT/info-hub.md"
LOCK_FILE="$PROJECT_ROOT/.info-hub.lock"
BACKUP_DIR="$PROJECT_ROOT/.info-hub.backup"
ARCHIVE_DIR="$PROJECT_ROOT/.info-hub.archive"

# Function to print colored output
print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Function to acquire lock
acquire_lock() {
    local max_wait=30
    local wait_time=0
    
    while [ -f "$LOCK_FILE" ] && [ $wait_time -lt $max_wait ]; do
        print_warning "Waiting for lock file to be released..."
        sleep 1
        ((wait_time++))
    done
    
    if [ -f "$LOCK_FILE" ]; then
        print_error "Could not acquire lock after ${max_wait} seconds"
        exit 1
    fi
    
    # Create lock file with timestamp and tool info
    echo "Locked at: $(date)" > "$LOCK_FILE"
    echo "Tool: $1" >> "$LOCK_FILE"
    echo "PID: $$" >> "$LOCK_FILE"
}

# Function to release lock
release_lock() {
    if [ -f "$LOCK_FILE" ]; then
        rm -f "$LOCK_FILE"
        print_status "Lock released"
    fi
}

# Cleanup on exit
trap release_lock EXIT

# Function to create backup
create_backup() {
    mkdir -p "$BACKUP_DIR"
    local timestamp=$(date +%Y%m%d_%H%M%S)
    cp "$INFO_HUB" "$BACKUP_DIR/info-hub_${timestamp}.md"
    print_status "Backup created: info-hub_${timestamp}.md"
}

# Function to update timestamp
update_timestamp() {
    local temp_file=$(mktemp)
    local current_date=$(date '+%Y-%m-%d %H:%M:%S UTC')
    
    # Update the Last Updated line
    sed "s/^\*Last Updated:.*\*/*Last Updated: $current_date*/" "$INFO_HUB" > "$temp_file"
    mv "$temp_file" "$INFO_HUB"
}

# Function to update work status
update_work_status() {
    local tool="$1"
    local task="$2"
    local status="$3"
    
    print_status "Updating work status for $tool"
    
    # Use awk to update the table
    local temp_file=$(mktemp)
    awk -v tool="$tool" -v task="$task" -v status="$status" -v date="$(date '+%Y-%m-%d %H:%M')" '
    BEGIN { in_table = 0; updated = 0 }
    /^### Currently Working/ { in_table = 1 }
    /^###/ && !/^### Currently Working/ { in_table = 0 }
    in_table && $0 ~ "^\\| " tool " \\|" {
        printf "| %-7s | %-30s | %-11s | %-12s |\n", tool, substr(task, 1, 30), status, date
        updated = 1
        next
    }
    { print }
    ' "$INFO_HUB" > "$temp_file"
    
    mv "$temp_file" "$INFO_HUB"
}

# Function to add to recent changes
add_recent_change() {
    local change="$1"
    local temp_file=$(mktemp)
    
    # Find the Recent Changes section and add the new change
    awk -v change="$change" '
    /^### Recent Changes/ { 
        print
        getline
        print
        print change
        next
    }
    { print }
    ' "$INFO_HUB" > "$temp_file"
    
    mv "$temp_file" "$INFO_HUB"
}

# Function to update metrics
update_metrics() {
    local temp_file=$(mktemp)
    
    # Count conflicts prevented (increment by 1 if no conflicts detected)
    local conflicts=$(grep -oP 'Conflicts Prevented: \K\d+' "$INFO_HUB" || echo "0")
    ((conflicts++))
    
    # Update metrics section
    sed -i "s/Conflicts Prevented: [0-9]*/Conflicts Prevented: $conflicts/" "$INFO_HUB"
}

# Function to check for conflicts
check_conflicts() {
    print_status "Checking for potential conflicts..."
    
    # Check if multiple tools are marked as "Working"
    local working_count=$(grep -c "| Working" "$INFO_HUB" || echo 0)
    
    if [ $working_count -gt 1 ]; then
        print_warning "Multiple tools detected as 'Working'. Potential conflict!"
        return 1
    fi
    
    return 0
}

# Function to archive old backups
archive_old_backups() {
    mkdir -p "$ARCHIVE_DIR"
    
    # Archive backups older than 24 hours
    find "$BACKUP_DIR" -name "info-hub_*.md" -mtime +1 -exec mv {} "$ARCHIVE_DIR/" \;
    
    # Keep only last 24 hourly backups
    local backup_count=$(ls -1 "$BACKUP_DIR"/info-hub_*.md 2>/dev/null | wc -l)
    if [ $backup_count -gt 24 ]; then
        ls -1t "$BACKUP_DIR"/info-hub_*.md | tail -n +25 | xargs rm -f
    fi
}

# Function to show usage
show_usage() {
    echo "Usage: $0 [command] [options]"
    echo ""
    echo "Commands:"
    echo "  status <tool> <task> <status>   Update work status for a tool"
    echo "  change <description>             Add a recent change"
    echo "  check                           Check for conflicts"
    echo "  backup                          Create a backup"
    echo "  metrics                         Update metrics"
    echo ""
    echo "Options:"
    echo "  -h, --help                      Show this help message"
    echo ""
    echo "Examples:"
    echo "  $0 status Claude 'Implementing feature X' 'In Progress'"
    echo "  $0 change '1. Updated planning-workflow.mdc with EARS format'"
    echo "  $0 check"
}

# Main execution
main() {
    if [ $# -eq 0 ] || [ "$1" == "-h" ] || [ "$1" == "--help" ]; then
        show_usage
        exit 0
    fi
    
    local command="$1"
    shift
    
    case "$command" in
        status)
            if [ $# -lt 3 ]; then
                print_error "Usage: $0 status <tool> <task> <status>"
                exit 1
            fi
            
            local tool="$1"
            local task="$2"
            local status="$3"
            
            acquire_lock "$tool"
            create_backup
            update_timestamp
            update_work_status "$tool" "$task" "$status"
            update_metrics
            archive_old_backups
            
            print_success "Info-hub updated successfully"
            ;;
            
        change)
            if [ $# -lt 1 ]; then
                print_error "Usage: $0 change <description>"
                exit 1
            fi
            
            local change="$1"
            
            acquire_lock "Script"
            create_backup
            update_timestamp
            add_recent_change "$change"
            
            print_success "Recent change added successfully"
            ;;
            
        check)
            check_conflicts
            if [ $? -eq 0 ]; then
                print_success "No conflicts detected"
            else
                print_error "Conflicts detected!"
                exit 1
            fi
            ;;
            
        backup)
            create_backup
            archive_old_backups
            print_success "Backup created and old backups archived"
            ;;
            
        metrics)
            acquire_lock "Script"
            update_metrics
            print_success "Metrics updated"
            ;;
            
        *)
            print_error "Unknown command: $command"
            show_usage
            exit 1
            ;;
    esac
}

# Run main function
main "$@"