package com.protasknubyyynx.service;

import com.protasknubyyynx.service.dto.StatusDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.protasknubyyynx.domain.Status}.
 */
public interface StatusService {
    /**
     * Save a status.
     *
     * @param statusDTO the entity to save.
     * @return the persisted entity.
     */
    StatusDTO save(StatusDTO statusDTO);

    /**
     * Updates a status.
     *
     * @param statusDTO the entity to update.
     * @return the persisted entity.
     */
    StatusDTO update(StatusDTO statusDTO);

    /**
     * Partially updates a status.
     *
     * @param statusDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<StatusDTO> partialUpdate(StatusDTO statusDTO);

    /**
     * Get all the statuses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StatusDTO> findAll(Pageable pageable);

    /**
     * Get the "id" status.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StatusDTO> findOne(Long id);

    /**
     * Delete the "id" status.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
