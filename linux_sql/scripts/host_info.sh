#! /bin/bash

#Setup arguments
psql_host=$1
psql_port=$2
psql_dbname=$3
psql_username=$4
psql_password=$5

#Validate arguments
if [ "$#" -ne 5 ]; then
	echo "Invalid parameters!"
	exit 1
fi

#To avoid password prompt,we set PGPASSWORD environment variable
export PGPASSWORD=$psql_password

#Reuse lscpu
lscpu_out=`lscpu`

#Parse host hardware specifications
hostname=$(hostname -f)
cpu_number=$(echo "$lscpu_out" | egrep "^CPU\(s\):" | awk '{print $2}' | xargs)
cpu_architecture=$(echo "$lscpu_out" | egrep "^Architecture:" | awk '{print $2}' | xargs)
cpu_model=$(echo "$lscpu_out" | egrep "^Model name:" | awk '{print $3,$4,$5,$6,$7}' |xargs)
cpu_mhz=$(echo "$lscpu_out" | egrep "^CPU MHZ:" | awk '{print $3}' | xargs)
L2_cache=$(echo "$lscpu_out" | egrep "^L2 Cache:" | awk -F '[ K]*' '{print $3}' |xargs) 
total_mem=$(cat /proc/meminfo | grep "^MemTotal" | awk '{print $2}' | xargs)
timestamp=$(date '+%Y-%m-%d %H:%M:%S')

#Construct INSERT statement. 
ins_stmt=$(cat << EOF
INSERT into host_info (hostname, cpu_number, cpu_architecture, cpu_model, cpu_mhz, L2_cache, total_mem, "timestamp")
VALUES('$hostname', $cpu_number, '$cpu_architecture', '$cpu_model', $cpu_mhz, $L2_cache, $total_mem, '$timestamp')
EOF
)

#Execute INSERT statement through plsql CLI
psql -h $psql_host -p $psql_port -d $psql_dbname -U $psql_username -c "$ins_stmt"

#Successful data insertion message
echo "Data insertion successful!"
exit 0


