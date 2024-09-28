package com.mchlt.mayor.service.writer;

import com.mchlt.mayor.model.Person;
import com.mchlt.mayor.service.PersonTownHall;
import lombok.AllArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class PersonItemWriter implements ItemWriter<PersonTownHall> {

    private final JdbcBatchItemWriter<Person> delegate;

    @Override
    public void write(Chunk<? extends PersonTownHall> chunk) throws Exception {
        List<Person> persons = chunk.getItems().stream()
            .map(PersonTownHall::getPerson)
            .collect(Collectors.toList());
        delegate.write(new Chunk<>(persons));
    }

}

