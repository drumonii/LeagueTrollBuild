package com.drumonii.loltrollbuild.test.batch;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StepExecutionRowMapper implements RowMapper<StepExecution> {

    private JobExecution jobExecution;

    public StepExecutionRowMapper(JobExecution jobExecution) {
        this.jobExecution = jobExecution;
    }

    @Override
    public StepExecution mapRow(ResultSet rs, int rowNum) throws SQLException {
        StepExecution stepExecution = new StepExecution(rs.getString("STEP_NAME"), jobExecution, rs.getLong("STEP_EXECUTION_ID"));
        stepExecution.setStartTime(rs.getTimestamp("START_TIME"));
        stepExecution.setEndTime(rs.getTimestamp("END_TIME"));
        stepExecution.setStatus(BatchStatus.valueOf(rs.getString("STATUS")));
        stepExecution.setCommitCount(rs.getInt("COMMIT_COUNT"));
        stepExecution.setReadCount(rs.getInt("READ_COUNT"));
        stepExecution.setFilterCount(rs.getInt("FILTER_COUNT"));
        stepExecution.setWriteCount(rs.getInt("WRITE_COUNT"));
        stepExecution.setExitStatus(new ExitStatus(rs.getString("EXIT_CODE"), rs.getString("EXIT_MESSAGE")));
        stepExecution.setReadSkipCount(rs.getInt("READ_SKIP_COUNT"));
        stepExecution.setWriteSkipCount(rs.getInt("WRITE_SKIP_COUNT"));
        stepExecution.setProcessSkipCount(rs.getInt("PROCESS_SKIP_COUNT"));
        stepExecution.setRollbackCount(rs.getInt("ROLLBACK_COUNT"));
        stepExecution.setLastUpdated(rs.getTimestamp("LAST_UPDATED"));
        stepExecution.setVersion(rs.getInt("VERSION"));
        return stepExecution;
    }

}
