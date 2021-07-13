package com.ezc.hsil.webapp.persistance.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezc.hsil.webapp.model.EzDocComments;

public interface EzDocCommentsRepo extends JpaRepository<EzDocComments, Integer>
{

}
