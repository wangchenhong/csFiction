1. 初始状态下，MySQL 只需要执行 `novel.sql` 文件即可正常运行本系统
2. 代码更新后再执行以日期命名的增量 SQL 文件
3. 只有开启 XXL-JOB 的功能，才需要执行 `xxl-job.sql` 和以 xxl-job 开头日期结尾的增量 SQL 文件
4. 只有开启 ShardingSphere-JDBC 的功能，才需要执行 `shardingsphere-jdbc.sql` 和以 shardingsphere-jdbc 开头日期结尾的增量 SQL
   文件

