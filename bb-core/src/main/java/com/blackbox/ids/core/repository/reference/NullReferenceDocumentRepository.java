package com.blackbox.ids.core.repository.reference;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.blackbox.ids.core.model.reference.NullReferenceDocument;

/**
 * The Interface NullReferenceDocumentRepository.
 */
public interface NullReferenceDocumentRepository extends JpaRepository<NullReferenceDocument, Long> {

	/**
	 * 
	 * @param nplString
	 * @param nullReference
	 * @param correspondenceID
	 */
	@Modifying
	@Query("Update NullReferenceDocument nullReferenceDocument set nullReferenceDocument.nullReference =:nullReference, nullReferenceDocument.nplString =:nplString where nullReferenceDocument.correspondenceID =:correspondenceID")
	void updateNPLStringAndNullReferenceFlagByCorrespondenceId(@Param("nplString") String nplString, @Param("nullReference") boolean nullReference,
			@Param("correspondenceID") Long correspondenceID);
	
	/**
	 * 
	 * @param nullReference
	 * @param correspondenceID
	 */
	@Modifying
	@Query("Update NullReferenceDocument nullReferenceDocument set nullReferenceDocument.nullReference =:nullReference where nullReferenceDocument.correspondenceID =:correspondenceID")
	void updateNullReferenceFlagByCorrespondenceId(@Param("nullReference") boolean nullReference,
			@Param("correspondenceID") Long correspondenceID);
	

	@Query("Select nullReferenceDocument from NullReferenceDocument nullReferenceDocument where nullReferenceDocument.correspondenceID =:correspondenceID")
	NullReferenceDocument getNullReferenceByCorrespondenceId(@Param("correspondenceID") Long correspondenceID);
	
	
}
