package com.protasknubyyynx.service.impl;

import com.protasknubyyynx.domain.Status;
import com.protasknubyyynx.repository.StatusRepository;
import com.protasknubyyynx.service.StatusService;
import com.protasknubyyynx.service.dto.StatusDTO;
import com.protasknubyyynx.service.mapper.StatusMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.protasknubyyynx.domain.Status}.
 */
@Service
@Transactional
public class StatusServiceImpl implements StatusService {

    private static final Logger LOG = LoggerFactory.getLogger(StatusServiceImpl.class);

    private final StatusRepository statusRepository;

    private final StatusMapper statusMapper;

    public StatusServiceImpl(StatusRepository statusRepository, StatusMapper statusMapper) {
        this.statusRepository = statusRepository;
        this.statusMapper = statusMapper;
    }

    @Override
    public StatusDTO save(StatusDTO statusDTO) {
        LOG.debug("Request to save Status : {}", statusDTO);
        Status status = statusMapper.toEntity(statusDTO);
        status = statusRepository.save(status);
        return statusMapper.toDto(status);
    }

    @Override
    public StatusDTO update(StatusDTO statusDTO) {
        LOG.debug("Request to update Status : {}", statusDTO);
        Status status = statusMapper.toEntity(statusDTO);
        status = statusRepository.save(status);
        return statusMapper.toDto(status);
    }

    @Override
    public Optional<StatusDTO> partialUpdate(StatusDTO statusDTO) {
        LOG.debug("Request to partially update Status : {}", statusDTO);

        return statusRepository
            .findById(statusDTO.getId())
            .map(existingStatus -> {
                statusMapper.partialUpdate(existingStatus, statusDTO);

                return existingStatus;
            })
            .map(statusRepository::save)
            .map(statusMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StatusDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Statuses");
        return statusRepository.findAll(pageable).map(statusMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StatusDTO> findOne(Long id) {
        LOG.debug("Request to get Status : {}", id);
        return statusRepository.findById(id).map(statusMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Status : {}", id);
        statusRepository.deleteById(id);
    }
}
