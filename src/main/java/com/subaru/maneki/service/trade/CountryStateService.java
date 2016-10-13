package com.subaru.maneki.service.trade;

import java.util.List;
import java.util.Map;

import com.subaru.maneki.model.Country;
import com.subaru.maneki.model.CountryState;

/**
 * @author lee
 *
 */
public interface CountryStateService {
	
	/**
	 * 根据国家的id列表，获得以国家、相应省份信息的map信息
	 * @param countryIdList
	 * @return
	 */
	Map<Integer, List<CountryState>> getCountryState(List<Integer> countryIdList);
	
	/**
	 * 根据国家的简写，获得国家信息
	 * @param abbr
	 * @return
	 */
	Country getCountryByAbbr(String abbr);
	
	/**
	 * 根据国家的id获取国家信息
	 * @param id
	 * @return
	 */
	Country getCountry(int id);
	
	/**
	 * 根据省份的id获取具体省份的信息
	 * @param id
	 * @return
	 */
	CountryState getState(int id);
}
