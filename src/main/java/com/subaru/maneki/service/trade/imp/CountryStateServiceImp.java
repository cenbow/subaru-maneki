package com.subaru.maneki.service.trade.imp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.subaru.maneki.dao.CountryDao;
import com.subaru.maneki.dao.CountryStateDao;
import com.subaru.maneki.model.Country;
import com.subaru.maneki.model.CountryState;
import com.subaru.maneki.service.trade.CountryStateService;


@Service("countryStateService")
public class CountryStateServiceImp implements CountryStateService{
	
	@Resource
	private CountryDao		countryDao;
	
	@Resource
	private CountryStateDao countryStateDao;
	
	public Map<Integer, List<CountryState>> getCountryState(List<Integer> countryIdList){
		Map<Integer, List<CountryState>> countryStateMap = new HashMap<>();
    	for (Integer id : countryIdList){
    		countryStateMap.put(id, countryStateDao.selectByCountryId(id));
    	}
		return countryStateMap;
	}
	
	public Country getCountryByAbbr(String abbr){
		return countryDao.selectByAbbr(abbr);
	}
	
	public Country getCountry(int id){
		return countryDao.select(id);
	}
	
	public CountryState getState(int id){
		return countryStateDao.select(id);
	}
}
