package com.subaru.maneki.dao;

import java.util.List;

import com.subaru.maneki.model.CountryState;

public interface CountryStateDao {

    int insert(CountryState countryState);

    int update(CountryState countryState);

    int delete(int countryId);

    CountryState select(int id);

    CountryState selectByName(String name);

    List<CountryState> selectByCountryId(int countryId);

    List<CountryState> selectByCountryIdList(List<Integer> countryIdList);
}
