package org.viacode.library.db.dao;

import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;

import org.viacode.library.db.model.Client;

/**
 * VIAcode
 * Created by IVolkov on 8/6/2014.
 */
@Transactional
@Repository
public class ClientDAO extends BaseDAOImpl<Client> {
}
