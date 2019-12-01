/*SQL QUERIES*/
--Question 1
select 
  cpu_number, 
  id as host_id, 
  total_mem 
from 
  host_info 
order by 
  cpu_number, 
  total_mem desc;
--Question 2
select 
  host_usage.host_id, 
  host_info.hostname, 
  host_info.total_mem, 
  avg (
    host_info.total_mem - host_usage.memory_free
  )/ host_info.total_mem
) * 100
) as used_memory percentage 
FROM 
  host_usage 
  INNER JOIN host_info on host_usage.host_id = host_info.id 
GROUP BY 
  host_usage.host_id, 
  host_info.hostname, 
  host_info.total_mem, 
  Date trunc('hour', timestamp) + date_part('minute', timestamp) :: int / 5 * interval '5 min' as new_timestamp 
ORDER BY 
  host_usage.id;
