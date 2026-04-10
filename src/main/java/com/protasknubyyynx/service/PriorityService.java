package com.protasknubyyynx.service;

import com.protasknubyyynx.service.dto.PriorityDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.protasknubyyynx.domain.Priority}.
 */
public interface PriorityService {
    /**
     * Save a priority.
     *
     * @param priorityDTO the entity to save.
     * @return the persisted entity.
     */
    PriorityDTO save(PriorityDTO priorityDTO);

    /**
     * Updates a priority.
     *
     * @param priorityDTO the entity to update.
     * @return the persisted entity.
     */
    PriorityDTO update(PriorityDTO priorityDTO);

    /**
     * Partially updates a priority.
     *
     * @param priorityDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PriorityDTO> partialUpdate(PriorityDTO priorityDTO);

    /**
     * Get all the priorities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PriorityDTO> findAll(Pageable pageable);

    /**
     * Get the "id" priority.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PriorityDTO> findOne(Long id);

    /**
     * Delete the "id" priority.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
