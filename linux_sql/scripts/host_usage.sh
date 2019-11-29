#! /bin/bash

#Validating parameters
if [ "$#" -ne 5 ]; then
	echo "Invalid parameters"
	exit 1
fi

#Assign arguments
psql_host=$1
psql_port=$2
psql_dbname=$3
psql_username=$4
psql_password=$5

#Set PGPASSWORD env variable
export PGPASSWORD=$psql_password

#Reuse command
vmstat_mb=$(vmstat --unit M)
vmstat_disk=$(vmstat -d)

#Parse server CPU and memory
timestamp=$(date '+%Y-%m-%d %H:%M:%S')
hostname=$(hostname -f)
memory_free=$(echo "$vmstat_mb" | grep -E --invert-match "procs|r" | awk '{print $4}' | xargs)
cpu_idel=$(echo "$vmstat_mb" | grep -E --invert-match "procs|r" | awk '{print $15}' | xargs)
cpu_kernel=$(echo "$vmstat_mb" | grep -E --invert-match "procs|r" | awk '{print $14}' | xargs)
disk_io=$(echo "$vmstat_d" | grep -E --invert-match "disk|*total" | awk '{print $10}' | xargs)
disk_available=$(df -BM / | grep -E --invert-match "Filesystem" | awk -F '[M]*' '{print $4}' | xargs)

#Construct INSERT statement
ins_stmt=$(cat << EOF
INSERT INTO host_usage ("timestamp",host_id,memory_free,cpu_idel,cpu_kernel,disk_io,disk_available)
VALUES ('$timestamp',(SELECT id FROM host_info WHERE hostname='$hostname'),$memory_free,$cpu_idel,$cpu_kernel,$disk_io,$disk_available)
EOF
)

#Execute INSERT statement
psql -h $psql_host -p $psql_port -d $psql_dbname -U $psql_username -c "$ins_stmt
"

#Successful message
echo "Data inserted successful!"
exit 0
