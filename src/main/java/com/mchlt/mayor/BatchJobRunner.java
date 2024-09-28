package com.mchlt.mayor;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BatchJobRunner implements CommandLineRunner {

    private final JobLauncher jobLauncher;
    private final Job job;

    public BatchJobRunner(JobLauncher jobLauncher, Job job) {
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    // Will be triggered at application start
    @Override
    public void run(String... args) throws Exception {
        System.out.println("*********** PREPARE JOB ************");
        Thread.sleep(3000);
        System.out.println("*********** RUN JOB ************");

        String inputFile = "mayors.csv";

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("inputFile", inputFile)
                .toJobParameters();
        jobLauncher.run(job, jobParameters);

        System.out.println("*********** JOB ENDED ************");
        Thread.sleep(10000000);
    }
}

