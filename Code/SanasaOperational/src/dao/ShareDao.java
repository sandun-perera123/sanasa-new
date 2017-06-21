/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Share;
import entity.Smember;

/**
 *
 * @author Sandun
 */
public class ShareDao {

    public static void add(Share share) throws DaoException {
        CommonDao.insert(share);
    }

    public static Long getTotalCount() {

        Long totalCount = 0L;

        totalCount = (Long) CommonDao.select("Sharegain.findTotalCount").get(0);

        return totalCount;
    }

}
