--DROP database if exists and CREATE a new database
DROP DATABASE if exists host_agent;

--CREATE a new database
CREATE DATABASE host_agent;

--switch to host_agent database
\c host_agent

--CREATE table host_info() to store hardware specifications
--host_info() - 'id' is PRIMARY KEY constraint
-- 		'hostname' is UNIQUE constraint
CREATE TABLE PUBLIC.host_info
	(
		id SERIAL NOT NULL PRIMARY KEY,
		hostname VARCHAR NOT NULL UNIQUE,
		cpu_number INTEGER NOT NULL,
		cpu_architecture VARCHAR NOT NULL,
		cpu_model VARCHAR NOT NULL,
		cpu_mhz FLOAT NOT NULL,
		L2_cache INTEGER NOT NULL,
		total_mem INTEGER NOT NULL,
		"timestamp" TIMESTAMP NOT NULL
	);

--CREATE table host_usage() to store resource usage data
CREATE TABLE PUBLIC.host_usage
	(
		"timestamp" TIMESTAMP NOT NULL,
		host_id SERIAL REFERENCES host_info(id),
		memory_free INTEGER NOT NULL,
		cpu_idel INTEGER NOT NULL,
		cpu_kernel INTEGER NOT NULL,
		disk_io INTEGER NOT NULL,
		disk_available INTEGER NOT NULL
	);



