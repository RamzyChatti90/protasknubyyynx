package com.protasknubyyynx.service.impl;

import com.protasknubyyynx.domain.Priority;
import com.protasknubyyynx.repository.PriorityRepository;
import com.protasknubyyynx.service.PriorityService;
import com.protasknubyyynx.service.dto.PriorityDTO;
import com.protasknubyyynx.service.mapper.PriorityMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.protasknubyyynx.domain.Priority}.
 */
@Service
@Transactional
public class PriorityServiceImpl implements PriorityService {

    private static final Logger LOG = LoggerFactory.getLogger(PriorityServiceImpl.class);

    private final PriorityRepository priorityRepository;

    private final PriorityMapper priorityMapper;

    public PriorityServiceImpl(PriorityRepository priorityRepository, PriorityMapper priorityMapper) {
        this.priorityRepository = priorityRepository;
        this.priorityMapper = priorityMapper;
    }

    @Override
    public PriorityDTO save(PriorityDTO priorityDTO) {
        LOG.debug("Request to save Priority : {}", priorityDTO);
        Priority priority = priorityMapper.toEntity(priorityDTO);
        priority = priorityRepository.save(priority);
        return priorityMapper.toDto(priority);
    }

    @Override
    public PriorityDTO update(PriorityDTO priorityDTO) {
        LOG.debug("Request to update Priority : {}", priorityDTO);
        Priority priority = priorityMapper.toEntity(priorityDTO);
        priority = priorityRepository.save(priority);
        return priorityMapper.toDto(priority);
    }

    @Override
    public Optional<PriorityDTO> partialUpdate(PriorityDTO priorityDTO) {
        LOG.debug("Request to partially update Priority : {}", priorityDTO);

        return priorityRepository
            .findById(priorityDTO.getId())
            .map(existingPriority -> {
                priorityMapper.partialUpdate(existingPriority, priorityDTO);

                return existingPriority;
            })
            .map(priorityRepository::save)
            .map(priorityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PriorityDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Priorities");
        return priorityRepository.findAll(pageable).map(priorityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PriorityDTO> findOne(Long id) {
        LOG.debug("Request to get Priority : {}", id);
        return priorityRepository.findById(id).map(priorityMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Priority : {}", id);
        priorityRepository.deleteById(id);
    }
}
