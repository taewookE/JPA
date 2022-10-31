package com.example.jpa.bookmanager.domain.converter;

import com.example.jpa.bookmanager.repository.dto.BookStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import javax.swing.text.AbstractDocument;

//TODO: AttributeConverter 매개변수의 첫번째는 Entity의 Attribute , 두번째는 db의 columnType GenericType에서는 WrapperedType으로 선언해야하므로 Integer로 입력
@Converter(autoApply = true)
public class BookStatusConverter implements AttributeConverter<BookStatus,Integer> {
    @Override
    public Integer convertToDatabaseColumn(BookStatus attribute) {
        return attribute.getCode();
    }

    @Override
    public BookStatus convertToEntityAttribute(Integer dbData) {
        return dbData != null ? new BookStatus(dbData) : null;
    }
}
