package com.mchlt.mayor.service;

import com.mchlt.mayor.model.Person;
import com.mchlt.mayor.model.TownHall;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class JobCompletionNotificationListener implements JobExecutionListener {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println("Import mayors from file \"" + jobExecution.getJobParameters().getString("inputFile") + "\"");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            System.out.println("SUCCESS");
            System.out.println("*********** VERIFY JOB ************");

            List<Person> people = jdbcTemplate.query("SELECT surname, name, birth_date, job FROM person",
                (rs, row) -> new Person(
                    rs.getString(1),
                    rs.getString(2),
                    rs.getDate(3).toLocalDate(),
                    rs.getString(4),
                    null, null, null, null,
                    null, null, null, null, null, null
                )
            );
            System.out.println("Added " + people.size() + " Person items in database");

            List<TownHall> townHalls = jdbcTemplate.query("SELECT * FROM town_hall",
                (rs, row) -> new TownHall(
                    rs.getString(1), rs.getString(2), rs.getString(3),
                    rs.getString(4), rs.getString(5)
                )
            );
            System.out.println("Added " + townHalls.size() + " TownHall items in database");
        } else if (jobExecution.getStatus() == BatchStatus.FAILED) {
            System.out.println("FAILED");
        }
    }
}
