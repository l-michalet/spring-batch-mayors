package com.mchlt.mayor.config;

import com.mchlt.mayor.model.Person;
import com.mchlt.mayor.model.TownHall;
import com.mchlt.mayor.service.JobCompletionNotificationListener;
import com.mchlt.mayor.service.PersonTownHall;
import com.mchlt.mayor.service.PersonTownHallProcessor;
import com.mchlt.mayor.service.writer.PersonItemWriter;
import com.mchlt.mayor.service.writer.TownHallItemWriter;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

@Configuration
@AllArgsConstructor
public class BatchConfig {

    /////////////////////////////////////////////////////////////////////////////////////////////////
    // Configurer le Job

    // Note: j'essayais au depart d'avoir JobRepository, PlatformTransactionManager et DataSource comme dependance de BatchConfig
    // Cependant le lien avec la db se faisait mal, et les tables liees a SpringBatch n'etaient pas crees

    @Bean
    public Job importJob(JobRepository jobRepository, JobCompletionNotificationListener listener, Step step1) {
        return new JobBuilder("job", jobRepository)
            .listener(listener)
            .incrementer(new RunIdIncrementer()) // Permet de relancer le job avec de nouveaux paramètres
            .start(step1)
            .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                      FlatFileItemReader<String[]> reader, CompositeItemWriter<PersonTownHall> writer,
                      PersonTownHallProcessor processor) {
        return new StepBuilder("step1", jobRepository)
            .<String[], PersonTownHall>chunk(10, transactionManager) // Les lignes seront traitees et sauvegardees par paquet de 10
            .reader(reader)
            .processor(processor)
            .writer(writer)
            .build();
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////
    // Reader: Lis le fichier CSV et mappe les fields

    @StepScope // Permet d'instancier le bean qu'au lancement du step, sinon ca throw parce que JobParameters n'est pas defini
    @Bean
    public FlatFileItemReader<String[]> reader(@Value("#{jobParameters['inputFile']}") String inputFile) {
        return new FlatFileItemReaderBuilder<String[]>()
            .name("csvReader")
            .resource(new ClassPathResource(inputFile))
            .linesToSkip(1) // Le CSV contient un titre
            .delimited()
            .delimiter(";")
            .names("surname", "name", "birthDate", "job", "jobType", "hiringDate", "jobStartDate",
                    "jobCategory", "jobCategorySubType", "nationality", "sex", "area", "areaCode",
                    "department", "departmentCode", "departmentSection", "departmentSectionCode",
                    "EPCI", "EPCICode", "city", "cityCode", "canton", "cantonName",
                    "circumscription", "circumscriptionCode", "collectivity", "collectivityCode")
            .fieldSetMapper(FieldSet::getValues)
            .build();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    // Processor: Map les donnees en objets Java, c'est la que les transformations sont faites

    @Bean
    public PersonTownHallProcessor processor() {
        return new PersonTownHallProcessor();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    // Writer: Sauvegarde les donnees dans la base H2
    // Il s'agit d'un CompositeWrite, il sauvegarde deux objets en meme temps

    @Bean
    @StepScope
    public JdbcBatchItemWriter<Person> personWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Person>()
            .beanMapped()
            .dataSource(dataSource)
            .sql("INSERT INTO person (surname, name, birth_date, job, job_type, hiring_date, job_start_date, job_category, job_category_sub_type, nationality, sex, area, department, city) " +
                    "VALUES (:surname, :name, :birthDate, :job, :jobType, :hiringDate, :jobStartDate, :jobCategory, :jobCategorySubType, :nationality, :sex, :area, :department, :city)")
            .build();
    }

    @Bean
    @StepScope
    public JdbcBatchItemWriter<TownHall> townHallWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<TownHall>()
            .beanMapped()
            .dataSource(dataSource)
            .sql("INSERT INTO town_hall (area, area_code, department, city, city_code) " +
                    "VALUES (:area, :areaCode, :department, :city, :cityCode)")
            .build();
    }

    @Bean
    @StepScope
    public CompositeItemWriter<PersonTownHall> writer(DataSource dataSource) {
        CompositeItemWriter<PersonTownHall> compositeItemWriter = new CompositeItemWriter<>();

        List<ItemWriter<? super PersonTownHall>> writers = Arrays.asList(
            new PersonItemWriter(personWriter(dataSource)),
            new TownHallItemWriter(townHallWriter(dataSource))
        );

        compositeItemWriter.setDelegates(writers);
        return compositeItemWriter;
    }

}
