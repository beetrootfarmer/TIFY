package com.tify.back.phonebook.service;

import com.tify.back.phonebook.dto.PhonebookDto;
import com.tify.back.phonebook.entity.Phonebook;
import com.tify.back.phonebook.repository.PhonebookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PhonebookService {

    private final PhonebookRepository phonebookRepository;

    public PhonebookService(PhonebookRepository phonebookRepository) {
        this.phonebookRepository = phonebookRepository;
    }

    public List<PhonebookDto> getAllPhonebookEntries() {
        List<Phonebook> phonebookEntries = phonebookRepository.findAll();
        return phonebookEntries.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public PhonebookDto addPhonebookEntry(PhonebookDto phonebookDto) {
        Phonebook phonebook = convertToEntity(phonebookDto);
        Phonebook savedPhonebook = phonebookRepository.save(phonebook);
        return convertToDto(savedPhonebook);
    }

    private PhonebookDto convertToDto(Phonebook phonebook) {
        PhonebookDto phonebookDto = new PhonebookDto();
        phonebookDto.setId(phonebook.getId());
        phonebookDto.setName(phonebook.getName());
        phonebookDto.setPhoneNumber(phonebook.getPhoneNumber());
        return phonebookDto;
    }

    private Phonebook convertToEntity(PhonebookDto phonebookDto) {
        Phonebook phonebook = new Phonebook();
        phonebook.setId(phonebookDto.getId());
        phonebook.setName(phonebookDto.getName());
        phonebook.setPhoneNumber(phonebookDto.getPhoneNumber());
        return phonebook;
    }

    public void deletePhonebookEntry(Long id) {
        phonebookRepository.deleteById(id);
    }
}
