package com.subaru.maneki.executor;

import java.sql.*;
import java.util.*;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.subaru.maneki.dao.*;
import com.subaru.maneki.enumeration.PropType;
import com.subaru.maneki.enumeration.RegisterType;
import com.subaru.maneki.model.*;
import com.subaru.maneki.service.product.SpuService;
import com.subaru.maneki.vo.ProductVO;
import com.subaru.maneki.vo.PropVO;
import com.subaru.maneki.vo.SkuVO;
import com.subaru.maneki.vo.SpuVO;
import com.subaru.core.BaseExecute;


/**
 * @author zhangchaojie
 * @since 2016-08-23
 */
public class Postgre2MysqlExecutor extends BaseExecute {

    @Resource
    private CateDao             cateDao;

    @Resource
    private SpuDao              spuDao;

    @Resource
    private SkuDao              skuDao;

    @Resource
    private SkuEtcDao           skuEtcDao;

    @Resource
    private ImageDao            imageDao;

    @Resource
    private SpuImageRelationDao spuImageRelationDao;

    @Resource
    private PropNameDao         propNameDao;

    @Resource
    private PropValueDao        propValueDao;

    @Resource
    private PropDao             propDao;

    @Resource
    private SkuPropDao          skuPropDao;

    @Resource
    private PropViewDao         propViewDao;

    @Resource
    private CountryDao          countryDao;

    @Resource
    private AddressDao          addressDao;

    @Resource
    private CountryStateDao     countryStateDao;

    @Resource
    private SpuCateRelationDao  spuCateRelationDao;
    
    @Resource
    private SpuService			spuService;

    @Resource
    private UserDao             userDao;

    @Resource
    private UserEtcDao          userEtcDao;

    @Resource
    private UserOAuthDao        userOAuthDao;

    @Resource
    private TradeProductDao     tradeProductDao;

    @Resource
    private ShippingInfoDao     shippingInfoDao;

    @Resource
    private OrderDao            orderDao;

    private Connection          connection;
    
    private Connection          connection47;
    
    private Connection          connection112;

    private Logger              logger = LoggerFactory.getLogger(Postgre2MysqlExecutor.class);

    @Before
    public void setUp() {
        System.out.println("-------- PostgreSQL " + "JDBC Connection Testing ------------");

        try {

            Class.forName("org.postgresql.Driver");

        } catch (ClassNotFoundException e) {

            System.out.println("Where is your PostgreSQL JDBC Driver? "
                               + "Include in your library path!");
            e.printStackTrace();
            return;

        }

        System.out.println("PostgreSQL JDBC Driver Registered!");

        //连接120的ERP数据库服务器
        try {

            connection = DriverManager.getConnection("jdbc:postgresql://120.27.152.95:5432/odoo2c",
                "root", "123Yuanshuju456");

        } catch (SQLException e) {

            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return;

        }

        if (connection != null) {
            System.out.println("You made it, take control your database now!");
        } else {
            System.out.println("Failed to make connection!");
        }
        
        //连接112的Postgre数据库服务器
        try {
            connection112 = DriverManager.getConnection(
                "jdbc:postgresql://112.124.99.208:5432/web", "root", "123Yuanshuju456");
        } catch (SQLException se) {
            System.out.println("Connection to 47 Failed! Check output console");
            se.printStackTrace();
            return;
        }

        //连接47的Postgre数据库服务器
        try {
        	connection47 = DriverManager.getConnection(
        			"jdbc:postgresql://47.88.35.64:5432/web", "root", "123Yuanshuju456");
        } catch (SQLException se) {
            System.out.println("Connection to 47 Failed! Check output console");
            se.printStackTrace();
            return;
        }

        if (connection47 != null) {
            System.out.println("成功连接47的Postgresql数据库服务器");
        } else {
            System.out.println("不能连接47的Postgresql数据库服务器");
        }

    }

    @After
    public void setAfter() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSelect() {

        Statement statement = null;

        try {
            //            connection.setAutoCommit(false);
            statement = connection.createStatement();
            String sql = "select * from product_template where id < 20";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getString(1));
                System.out.println("C_PLATFORM_PRICE: " + resultSet.getString(2));
                System.out.println("LIST_PRICE: " + resultSet.getString(3));
                System.out.println("CAN_SHIPPED_TO_ME: " + resultSet.getString(4));
                System.out.println("WEIGHT: " + resultSet.getString(5));
                System.out.println("WRITE_UID: " + resultSet.getString(6));
                System.out.println("C_PLATFORM_URL: " + resultSet.getString(7));
                System.out.println();
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 1、取出product_public_category表中的记录，然后每读出一行，依次写到mysql的product_category表中
     */
    @Test
    public void transferCates() {
        //        Cate cate = getCateFromPostgre();
        Statement statement = null;
        Cate cate = null;
        ResultSet resultSet = null;
        ArrayList<Integer> idList = new ArrayList<Integer>();

        try {
            statement = connection.createStatement();
            String sql = "select * from product_public_category";
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                cate = new Cate();
                cate.setOldId(resultSet.getInt(1));
                cate.setName(resultSet.getString(4));
                cate.setParentId(resultSet.getInt(7));
                cate.setSequence(resultSet.getInt(5));

                saveCate2Mysql(cate);

                idList.add(cate.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                System.out.println("SYNC CATEGORY TABLE END");
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }

        //调整Mysql的product_category表中parentId的值
        for (int id : idList) {
            cate = cateDao.select(id);
            if (cate.getParentId() != 0) {
                int parentId = searchParentId(cate.getParentId());
                if (parentId > 0) {
                    cate.setParentId(parentId);
                    cateDao.update(cate);
                }
            }
        }

    }

    /**
     * 在product_category表中根据oldId的值找出对应的记录
     *
     * @param oldId
     * @return -1：没有找到相关记录
     * 0 ： 找到相关记录
     */
    private int searchParentId(int oldId) {
        Cate cate = cateDao.selectByOldId(oldId);
        if (cate == null) {
            System.out
                .println("Origin table product_public_category error, the oldId's related record is null "
                         + oldId + " !!");
            return -1;
        } else {
            return cate.getId();
        }
    }

    private List<Cate> getProductCategory() {
        List<Cate> cateList = cateDao.selectAll();

        return cateList;
    }

    private double max(double a1, double a2, double b1, double b2) {
        double m1 = Math.max(a1, a2);
        double m2 = Math.max(b1, b2);

        return Math.max(m1, m2);
    }

    private double min(double a1, double a2, double b1, double b2) {
        double m1 = Math.max(a1, a2);
        double m2 = Math.max(b1, b2);

        return Math.min(m1, m2);
    }

    /**
     * 迁移国家和具体省份/州的信息，包括countryId，国家全名，货币单位，国家缩写，汇率，运价
     * countryId : res_country的id
     * 国家全名：res_country中的name
     * 货币单位：res_currency中的name
     * 国家缩写：res_country中的code值
     * 汇率：从112数据库中读取当天的数据
     * 运费的规则：从delivery_carrier和delivery_price_rule中读取
     * <p>
     * 省份/州的信息主要从res_country_state中读取
     */
    @Test
    public void transferCountry(){
    	PreparedStatement countryStatement = null;
    	PreparedStatement deliveryStatement = null;
    	PreparedStatement exchangeRateStatement = null;
    	PreparedStatement stateStatement = null;
    	
    	ResultSet resultSet = null;
    	ResultSet deliveryResultSet = null;
    	ResultSet stateResultSet = null;
    	
    	Country country = null;
    	String sql = null;
    	CountryState countryState = null;
    	
    	Map<String, Double> exchangeRates = new HashMap<>();
    	
    	try{
    		//到47数据库服务器获取当天的汇率数据，形成一个hashmap结构
    		sql = "SELECT base, rates FROM exchange_rates ORDER BY TIMESTAMP DESC";
    		exchangeRateStatement = connection47.prepareStatement(sql);
    		resultSet = exchangeRateStatement.executeQuery();
    		if (resultSet.next() == false){
    			System.out.println("47的exchange_rates表中没有相关的汇率记录");
    			return;
    		}
    		String base = resultSet.getString(1);
			String rates = resultSet.getString(2);
    		
			exchangeRates = JSON.parseObject(rates, new TypeReference<Map<String, Double>>(){});
    		
    		//查询47数据库
    		sql = "SELECT res_country.id, res_country.name AS name, res_currency.name as currencyUnit, code FROM res_country, res_currency"
    				+ " WHERE res_country.currency_id = res_currency.id;";
    		countryStatement = connection47.prepareStatement(sql);
    		resultSet = countryStatement.executeQuery();
    		while(resultSet.next()){
    			int countryId = resultSet.getInt(1);
    			String name = resultSet.getString(2);
    			String currencyUnit = resultSet.getString(3);
    			String abbr  = resultSet.getString(4);
    			
    			//如果找不到汇率数据，那么直接跳过对这个国家的存储
    			if (exchangeRates.containsKey(currencyUnit) == false){
    				System.out.println(currencyUnit);
    				continue;
    			}
    			
    			//查看运费规则，其返回结果条数一般是两条
    			sql = "SELECT max_value, list_base_price FROM delivery_carrier_country_rel, delivery_price_rule "
                        + "WHERE delivery_carrier_country_rel.carrier_id = delivery_price_rule.carrier_id AND "
                        + "delivery_carrier_country_rel.country_id = ?";
    			deliveryStatement = connection.prepareStatement(sql);
    			deliveryStatement.setInt(1, countryId);
    			
    			double deliveryFee = 0.0;
    			double dollarThreshold = 0.0;
    			
    			deliveryResultSet = deliveryStatement.executeQuery();
    			
				List<Double> maxValue = new ArrayList<Double>();
				List<Double> listBasePrice = new ArrayList<Double>();
				while (deliveryResultSet.next()){
					maxValue.add(deliveryResultSet.getDouble(1));
					listBasePrice.add(deliveryResultSet.getDouble(2));
				}
				if (maxValue.size() == 0){
					dollarThreshold = 0.0;
					deliveryFee = 0.0;
				}else if(maxValue.size() == 2){
					dollarThreshold = this.max(maxValue.get(0), maxValue.get(1), listBasePrice.get(0), listBasePrice.get(1));
					deliveryFee = this.min(maxValue.get(0), maxValue.get(1), listBasePrice.get(0), listBasePrice.get(1));
				}else{
					dollarThreshold = Math.max(maxValue.get(0), listBasePrice.get(0));
					deliveryFee = Math.min(maxValue.get(0), listBasePrice.get(0));
				}
				
				country = new Country();
				country.setDollarThreshold(dollarThreshold);
				country.setDeliveryFee(deliveryFee);
				country.setAbbr(abbr);
				country.setCurrencyUnit(currencyUnit);
				country.setName(name);
				
//				System.out.println(currencyUnit);
				country.setRate(exchangeRates.get(currencyUnit));
				
				saveCountry2Mysql(country);
				
				//查询具体省份的信息
				sql = "SELECT code, name, country_id FROM res_country_state WHERE country_id = ?";
				stateStatement = connection47.prepareStatement(sql);
				stateStatement.setInt(1, countryId);
				stateResultSet = stateStatement.executeQuery();
				while (stateResultSet.next()){
					countryState = new CountryState();
					countryState.setCode(stateResultSet.getString(1));
					countryState.setName(stateResultSet.getString(2));
					//countryId是指country表的主键id
					//countryState.setCountryId(stateResultSet.getInt(3));
					countryState.setCountryId(country.getId());
					
					saveCountryState2Mysql(countryState);
				}
    		}
    	}catch(SQLException se){
    		se.printStackTrace();
    	}catch(Exception e){
    		e.printStackTrace();
    	}finally {
			try{
				if (countryStatement != null){
					countryStatement.close();
				}
				if (deliveryStatement != null){
					deliveryStatement.close();
				}
			}catch(SQLException se){
				se.printStackTrace();
			}
			
			System.out.println("国家信息迁移结束!");
		}
    }

    /**
     * 迁移用户地址的信息，这需要在迁移用户数据的时候也一并迁移
     */
    @Test
    public void transferAddress() {
    	
    }
    
    /**
     * 数据迁移策略
     * 按照id从小到大，分批次进行迁移，每批处理1000行数据，sql表现为
     * select * from product_template limit start, offset
     * 
     * 为了提高效率，导出的数据插入到mysql时，mysql可以考虑利用batch技术一次性插入
     */
    @Test
    public void initProducts(){

        int count = 0;
        int skipCount = 0;
        String sql = null;
        
        //配置信息
        int LIMIT_SIZE = 1000;
        boolean IS_NEED_SYNC_CATEGORY = true;
        int END = 10000;
                
    	try {
            System.out.println("BEGIN INIT PRODUCTS INTO TEST DB..........");
                        
            int start = 0;
            int index = 0;
            
            boolean isEnd = false;
            
            do{
            	
            	sql = "select * from product_template LIMIT " + LIMIT_SIZE + " OFFSET " + start;
            	System.out.println("****************************************************");
            	System.out.println("START =  : " + start);
            	System.out.println("EXECUTE SQL : " + sql);
            	
            	logger.debug("****************************************************");
            	logger.debug("START =  : " + start);
            	
            	List<Integer> templateIdList = executeTransferProduct(sql);
            	if (templateIdList == null){
            		break;
            	}
            	
            	
            	if (IS_NEED_SYNC_CATEGORY == true){
            		executeTransferProductCategory(templateIdList);
            	}
            	
            	count = count + templateIdList.size();
            	skipCount = skipCount + (LIMIT_SIZE - templateIdList.size());
            	
            	if(count >= END){
            		break;
            	}
            	
            	index++;
            	start = index * LIMIT_SIZE;
            }while(isEnd == false);
            
            if (IS_NEED_SYNC_CATEGORY == false){
            	if (executeTransferProductCategory(null) == false){
            		logger.debug("商品品类信息迁移有误");
            		System.out.println("商品品类信息迁移有误");
            	}
            }
            
        }catch (Exception e) {
        	e.printStackTrace();
		}finally {
			System.out.println("因为异常导致未同步的商品数量为" + skipCount);
            System.out.println("总的商品同步数量为" + count);
            System.out.println("SYNC END!!!");
		}
    }
    
    /**
     * 真正执行商品数据迁移的方法
     * @param sql
     * @return 
     * 成功迁移的id列表
     * 失败或者没有查询到数据时返回null
     */
    private List<Integer> executeTransferProduct(String sql){
    	
    	if (sql == null){
    		return null;
    	}
    	PreparedStatement productTemplateStatement = null;
        PreparedStatement productProductStatement = null;

        ResultSet resultSet = null;
        ProductVO productVo = null;

        int count = 0;
        List<Integer> idList = new ArrayList<>();        
        int TEST_COUNT = 1000;
                
    	try {
            
            connection.setAutoCommit(false);
            
            productTemplateStatement = connection.prepareStatement(sql);
            //当数据库里商品的记录比较多时，这里对商品采用流的方式进行读取，默认每次读取50个记录
            productTemplateStatement.setFetchSize(50);
            resultSet = productTemplateStatement.executeQuery();
            //查询结果是空，表明数据迁移完毕
            if (resultSet.next() == false){
            	return null;
            }
            resultSet = productTemplateStatement.executeQuery();
            while (resultSet.next()) {
            	if(count == TEST_COUNT){
                	break;
                }
            	int productTemplateId = resultSet.getInt(1);
            	productVo = parseProduct(resultSet, connection, logger);
            	
            	if (productVo == null){
            		logger.debug("product template id = " + productTemplateId + ": skip");
            		continue;
            	}
            	
            	if (productVo.getActive() == false){
            		logger.debug("product template id = " + productTemplateId + ": the product is not active");
            		continue;
            	}
            	
            	idList.add(productTemplateId);
            	count++;
            	
            	System.out.println("计算第" + (count) + "个商品，商品的id为" + productTemplateId
                        + "\n**************");
     
                /*
                 * 将productVo对象转换后插入到mysql表中
                 */
                try {
                    saveProduct2Mysql(productVo);
                } catch (Exception e) {
                    logger.debug("product_template_id = " + productTemplateId
                                 + " 数据长度过大导致Mysql插入失败! ");
                    System.out.println("product_template_id = " + productTemplateId + e);
                }
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (productTemplateStatement != null) {
                    productTemplateStatement.close();
                }
                if (productProductStatement != null) {
                    productProductStatement.close();
                }
               
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        
    	return idList;
    }
    
    /**
     * 迁移商品所属种类的数据
     * @param spuIdList
     * @return
     */
    private boolean executeTransferProductCategory(List<Integer> templateIdList){
    	
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String sql = null;
        logger.debug("开始同步商品所属种类信息");
        try {
        	sql = "select * from product_public_category_product_template_rel";
            statement = connection.prepareStatement(sql);
            
            resultSet = statement.executeQuery();
            SpuCateRelation spuCateRelation;
            int templateId = 0;
            while (resultSet.next()) {
                spuCateRelation = new SpuCateRelation();
                templateId = resultSet.getInt(1);
                if (templateIdList != null){
	                boolean flag = false;
	                for (int i = 0; i < templateIdList.size(); i++){
	                	if (templateId == templateIdList.get(i)){
	                		flag = true;
	                	}
	                }
	                if (false == flag){
	                	continue;
	                }
                }
                spuCateRelation = parseProductCategory(resultSet, logger);
                if (spuCateRelation != null){
                	//save
    	            spuCateRelationDao.insert(spuCateRelation);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                System.out.println("SYNC SPU_CATEGORY_RELATION TABLE END");
                logger.debug("同步商品所属种类信息结束");
            } catch (SQLException se) {
                se.printStackTrace();
                return false;
            }
        }
        
        return true;
    }

   
    @Test
    public void transferProducts() {
        PreparedStatement productTemplateStatement = null;
        PreparedStatement productProductStatement = null;

        ResultSet resultSet = null;
        ProductVO productVo = null;

        int count = 0;
        int skipCount = 0;
        String sql = null;
        List<Integer> idList = new ArrayList<>();
        
        int TEST_COUNT = 1000;

        try {
            System.out.println("BEGIN SYNC..........");
            
            connection.setAutoCommit(false);
      
            //sql = "select * from product_template where id = 104142";
            //根据最新更新的时间抓取数据
            sql = "select * from product_template order by write_date desc limit 5000";
            
            //获取jewelry数据
            //sql = "select * from product_template WHERE categ_id = 1002 AND active = true ORDER by write_date desc limit 100";
            
            //获取clothing数据
            //sql = "select * from product_template WHERE categ_id = 1001 AND active = true ORDER by write_date desc limit 100";
            
            //获取home数据
            //sql = "select * from product_template WHERE categ_id = 1011 AND active = true ORDER by write_date desc limit 100";
            
            productTemplateStatement = connection.prepareStatement(sql);
            //当数据库里商品的记录比较多时，这里对商品采用流的方式进行读取，默认每次读取50个记录
            productTemplateStatement.setFetchSize(50);
            resultSet = productTemplateStatement.executeQuery();
            while (resultSet.next()) {
            	int productTemplateId = resultSet.getInt(1);
            	count++;
            	if(count == TEST_COUNT){
                	break;
                }
            	System.out.println("计算第" + (count) + "个商品，商品的id为" + productTemplateId
                        + "\n**************");
     
            	productVo = parseProduct(resultSet, connection, logger);
            	
            	if (productVo == null){
            		logger.debug("product template id = " + productTemplateId + ": skip");
            		skipCount++;
            		continue;
            	}
            	
            	if (productVo.getActive() == false){
            		logger.debug("product template id = " + productTemplateId + ": the product is not active");
            		skipCount++;
            		continue;
            	}
            	
            	idList.add(productTemplateId);
            	
                /*
                 * 将productVo对象转换后插入到mysql表中
                 */
                try {
                    saveProduct2Mysql(productVo);
                } catch (Exception e) {
                    logger.debug("product_template_id = " + productTemplateId
                                 + " 数据长度过大导致Mysql插入失败! ");
                    System.out.println("product_template_id = " + productTemplateId + e);
                }
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (productTemplateStatement != null) {
                    productTemplateStatement.close();
                }
                if (productProductStatement != null) {
                    productProductStatement.close();
                }
               
            } catch (SQLException se) {
                se.printStackTrace();
            }
            System.out.println("因为异常导致未同步的商品数量为" + skipCount);
            System.out.println("总的商品同步数量为" + count);
            System.out.println("SYNC END!!!");
        }

        //spu_cate_relation表迁移
        PreparedStatement statement = null;
        try {
            sql = "select * from product_public_category_product_template_rel";
            statement = connection.prepareStatement(sql);

            resultSet = statement.executeQuery();
            SpuCateRelation spuCateRelation;
            int templateId = 0;
            while (resultSet.next()) {
                spuCateRelation = new SpuCateRelation();
                templateId = resultSet.getInt(1);

                boolean flag = false;
                for (int i = 0; i < idList.size(); i++) {
                    if (templateId == idList.get(i)) {
                        flag = true;
                    }
                }
                if (false == flag) {
                    continue;
                }
                spuCateRelation = parseProductCategory(resultSet, logger);
                if (spuCateRelation != null){
                	//save
    	            spuCateRelationDao.insert(spuCateRelation);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                System.out.println("SYNC SPU_CATEGORY_RELATION TABLE END");
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    private void saveCate2Mysql(Cate cate) {

        if (cate == null) {
            System.out.println("Transfer cate error, the given cate is null !!");
        }
        if (cate.getOldId() <= 0) {
            System.out.println("Transfer cate error, the oldId of given cate is " + cate.getOldId()
                               + " !!");
        }

        cateDao.insert(cate);

    }

    /**
     * 迁移res_country国家的信息到Mysql的user_country表中
     *
     * @param country
     */
    private void saveCountry2Mysql(Country country) {
        countryDao.insert(country);
    }

    /**
     * 迁移res_country_state省份/州的信息到Mysql的user_country_state表中
     *
     * @param countryState
     */
    private void saveCountryState2Mysql(CountryState countryState) {
        countryStateDao.insert(countryState);
    }

    private void saveProduct2Mysql(ProductVO productVO) {

        //1. SPU
        SpuVO spuVO = productVO.getSpuVO();
        if (spuVO == null) {
            System.out.println("Transfer product error, the spuVO of given productVO is null !!");
            return;
        }

        String cateName = spuVO.getCateName();
        if (StringUtils.isBlank(cateName)) {
            System.out
                .println("Transfer product error, the cateName of given productVO is null !!");
            return;
        }

        Cate cate = cateDao.selectByName(cateName);
        if (cate == null) {
            System.out
                .println("Transfer product error, cannot get cate by given cateName of productVO, the given cateName is "
                         + cateName + " !!");
            return;
        }

        Spu spu = new Spu(spuVO.getName(), cate.getId(), spuVO.getIsPublished(),
            spuVO.getProductNo(), spuVO.getSearchKeywords(), spuVO.getComment(), spuVO.getScore(),
            spuVO.getSold(), spuVO.getClusterId(), spuVO.getOldId(), spuVO.getOverallScore());

        //插入新的字段，表示新用户请求首页时商品展示的排序依据
        spu.setNewUserScore(spuVO.getNewUserScore());

        spuDao.insert(spu);

        //2. SKU
        List<SkuVO> skuVOList = productVO.getSkuVOList();
        if (skuVOList == null || skuVOList.size() <= 0) {
            System.out
                .println("Transfer product error, the skuVOList of given productVO is null or empty !!");
        }

        for (SkuVO skuVO : skuVOList) {
            Sku sku = new Sku(spu.getId(), skuVO.getPrice(), skuVO.getWeight(),
                skuVO.getCurrencyUnit(), skuVO.getInventory(), skuVO.getIsSaleOk(),
                skuVO.getOldId());
            skuDao.insert(sku);
            SkuEtc skuEtc = new SkuEtc(sku.getId(), skuVO.getPlatformPrice(),
                skuVO.getPlatformUrl(), skuVO.getPlatformPriceDetails(), skuVO.getPlatformName(),
                skuVO.getUrlOf1688());
            //System.out.println(skuVO.getUrlOf1688().length());
            skuEtcDao.insert(skuEtc);

            /**
             * zhangchaojie
             * 分析sku的属性
             * a. 获取<name, value>属性对
             * b. 查看name对应的propName记录是否存在，有则返回该对象，无则新插入记录
             * c. 查看value对应的propValue记录是否存在，有则返回该对象，无则新插入记录
             * d. 根据nameId和valueId查看prop记录是否存在，有则返回该对象，无则新插入记录
             * e. 新插入skuProp记录
             */
            //            Map<String, String> propMap = skuVO.getPropMap();
            List<PropVO> propList = skuVO.getPropList();
            for (PropVO propVO : propList) {
                String propNameStr = propVO.getPropName();
                PropName propName = propNameDao.selectByParentAndName(0, propNameStr);
                if (propName == null) {
                    propName = new PropName(propNameStr, 0);
                    propNameDao.insert(propName);
                }
                String propValueStr = propVO.getPropValue();
                PropValue propValue = propValueDao.selectByValue(propValueStr);
                if (propValue == null) {
                    propValue = new PropValue(propValueStr);
                    propValueDao.insert(propValue);
                }
                Prop prop = propDao.selectByNameIdAndValueId(propName.getId(), propValue.getId(),
                    PropType.SKU.getValue());
                if (prop == null) {
                    prop = new Prop(propName.getId(), propValue.getId(), PropType.SKU.getValue());
                    propDao.insert(prop);
                }
                SkuProp skuProp = skuPropDao.selectBySkuIdAndPropId(sku.getId(), prop.getId());
                if (skuProp == null) {
                    skuProp = new SkuProp(sku.getId(), prop.getId(), propVO.getSequence());
                    skuPropDao.insert(skuProp);
                }
            }
        }

        //3. IMAGE
        List<Image> imageList = spuVO.getImageList();
        for (Image image : imageList) {
            Image newImage = new Image(image.getUrl(), image.getSmallUrl(), image.getMiddleUrl(),
                image.getSequence(), image.getIsMain());
            imageDao.insert(newImage);
            SpuImageRelation spuImageRelation = new SpuImageRelation(spu.getId(), newImage.getId());
            spuImageRelationDao.insert(spuImageRelation);
        }

        //4. PROP，注意：spuPropJson和skuPropJson存在未空的情况
        String spuPropJson = spuVO.getSpuPropJson();
        String skuPropJson = spuVO.getSkuPropJson();
        if (StringUtils.isBlank(spuPropJson) || StringUtils.isBlank(skuPropJson)) {
            System.out
                .println("Transfer product error, the spuPropJson of given productVO is null !!");
            //            return;
        }
        PropView propView = new PropView(spu.getId(), spuPropJson, skuPropJson);
        propViewDao.insert(propView);

    }    
    
    /**
     * 解析从postgresql的商品表中获取得到的数据，至于这个数据要不要，这个是上层调用逻辑去判断的事
     * @param resultSet
     * @param connection
     * @param logger
     * @return
     */
    public ProductVO parseProduct(ResultSet resultSet, Connection connection, Logger logger){
        PreparedStatement productProductStatement = null;
        PreparedStatement skuStatement = null;
        PreparedStatement imageStatement = null;

        SpuVO spuVo = null;
        SkuVO skuVo = null;
        ProductVO productVo = new ProductVO();
        
        String sql = null;

    	try {
            spuVo = new SpuVO();
            skuVo = new SkuVO();
            List<SkuVO> skuVoList = new ArrayList<SkuVO>();
            productVo = new ProductVO();
            Map<String, Set<String>> skuPropMap = new HashMap<String, Set<String>>();

            int productTemplateId = resultSet.getInt(1);
            //为了以防万一，先判断下数据库中是否有同样template id的记录，效率上会受到影响
            Spu spu = spuDao.selectByOldId(productTemplateId);
            if (spu != null){
            	return null;
            }
            
            logger.debug("Testing product template id = " + productTemplateId);
            
            Double listPrice = resultSet.getDouble(3);
            int weight = resultSet.getInt(4);
            boolean saleOk = resultSet.getBoolean(12);
            int categId = resultSet.getInt(13); //parent
            boolean active = resultSet.getBoolean(23);
            //如果商品属性设置为false，那么直接不同步这个商品
            if(active == false){
            	productVo.setActive(false);
            	return productVo;
            }else{
            	productVo.setActive(true);
            }
            String name = resultSet.getString(25);
            String overview = resultSet.getString(34);
            String imageUrl = resultSet.getString(37);

            Double cPlatformPrice = resultSet.getDouble(38);
            String cPlatformUrl = resultSet.getString(39);
            String cPlatformName = resultSet.getString(40);
            boolean websitePublished = resultSet.getBoolean(46);

            int orders = resultSet.getInt(49);
            String bAverageRating = resultSet.getString(52);
            String bSpecifics = resultSet.getString(54);
            String cDetails = resultSet.getString(55);

            int clusterId = resultSet.getInt(56);
            String searchKeywords = resultSet.getString(57);
            int overallScore = resultSet.getInt(58);
            String urls1688 = resultSet.getString(59);

            String productNo = resultSet.getString(60);
            
            int newUserScore = resultSet.getInt(62);

            if (productNo == null) {
                logger.debug("product template id = " + productTemplateId
                             + " productNo is null");
                return null;
            }

            spuVo.setClusterId(clusterId);

            /*
             * 1. SPU
             */
            //根据categoryId从product_category表中获取相关记录，以此来获取名字，如果categoryId为0，那么直接进行下一个循环操作
            if (categId == 0) {
                logger.debug("product template id = " + productTemplateId + ":categoryId = 0");
                return null;
            }
        
            if (imageUrl == null) {
                logger.debug("product template id = " + productTemplateId
                             + ":the main image url is null");
            }
            //后面必须优化，保存名字就是一个馊主意
            Cate cate = cateDao.selectByOldId(categId);
            if (cate != null){
            	spuVo.setCateName(cate.getName());
            }
          
            spuVo.setComment(overview);

            //product_images（多）和product_template（一）是多对一的关系
            //目前product_template中的image_url是根据product_images表中sequence字段的大小来排序，取sequence最大的值作为image_url的值
            sql = "SELECT * FROM product_images WHERE product_template_id = ?";
            imageStatement = connection.prepareStatement(sql);
            imageStatement.setInt(1, productTemplateId);
            ResultSet imageResultSet = imageStatement.executeQuery();
            List<Image> imageList = new ArrayList<Image>();
            while (imageResultSet.next()) {
                Image image = new Image();
                int imageId = imageResultSet.getInt(1);
                //2 is create_uid
                //3 is create_date
                //这是设计上的错误，在postgresp的product_images表中SEQUENCE字段类型是string类型，而原本应该是int

                String url = imageResultSet.getString(4);
                String smallUrl = imageResultSet.getString(5);
                String middleUrl = imageResultSet.getString(6);
                int imageSequnce = imageResultSet.getInt(10);
                image.setUrl(url);
                image.setSmallUrl(smallUrl);
                image.setMiddleUrl(middleUrl);
                image.setSequence(imageSequnce);

                if (imageUrl != null && imageUrl.startsWith(url)) {
                    image.setIsMain(true);
                }

                imageList.add(image);
            }
            spuVo.setImageList(imageList);
            spuVo.setSold(orders);
            //按周学长的理解，isPublished字段可以看成websitePublished和active进行与操作的值，要考虑active值不存在的异常情况
            try {
                spuVo.setIsPublished(websitePublished && active);
            } catch (NullPointerException e) {
                spuVo.setIsPublished(websitePublished);
            }
            spuVo.setName(name);
            spuVo.setOldId(productTemplateId);
            spuVo.setProductNo(productNo);
            spuVo.setScore(bAverageRating);
            spuVo.setSearchKeywords(searchKeywords);
            spuVo.setSpuPropJson(bSpecifics);
            spuVo.setOverallScore(overallScore);
            spuVo.setNewUserScore(newUserScore);

            /*
             * 2. SKU
             */
            skuVo.setIsSaleOk(saleOk);
            String skuSql = "SELECT * FROM product_product WHERE product_tmpl_id = ? AND active = true";
            productProductStatement = connection.prepareStatement(skuSql);
            productProductStatement.setInt(1, productTemplateId);
            ResultSet skuResultSet = productProductStatement.executeQuery();
            
            //可能商品没有具体的sku，那么就要在sku表中手动插入需要的字段
            if (skuResultSet.next() == false){
            	skuVo = new SkuVO();
                //由于原有postgresql的表中没有和currentUnit和inventory对应的字段，因此暂时不需要set
                skuVo.setIsSaleOk(saleOk);
                skuVo.setOldId(0);
                skuVo.setPlatformName(cPlatformName);
                skuVo.setPlatformPrice(cPlatformPrice.toString());
                skuVo.setPlatformPriceDetails(cDetails);
                skuVo.setPlatformUrl(cPlatformUrl);
                skuVo.setPrice(listPrice);
                skuVo.setUrlOf1688(urls1688);
                skuVo.setWeight(weight);
                
                skuVoList.add(skuVo);
                spuVo.setSkuPropJson("{}");
            }else{
            	skuResultSet = productProductStatement.executeQuery();
            }
            while (skuResultSet.next()) {
                int productId = skuResultSet.getInt(1);
                //    				System.out.println("product id = " + productId);

                skuVo = new SkuVO();
                //由于原有postgresql的表中没有和currentUnit和inventory对应的字段，因此暂时不需要set
                skuVo.setIsSaleOk(saleOk);
                skuVo.setOldId(productId);
                skuVo.setPlatformName(cPlatformName);
                skuVo.setPlatformPrice(cPlatformPrice.toString());
                skuVo.setPlatformPriceDetails(cDetails);
                skuVo.setPlatformUrl(cPlatformUrl);
                skuVo.setPrice(listPrice);
                skuVo.setUrlOf1688(urls1688);
                skuVo.setWeight(weight);

                List<PropVO> propVoList = new ArrayList<PropVO>();

                /*
                 * 3. 根据productId取出该sku对应所有的属性，比如{size:'S', color:'red'}的情况
                 */
                String propSkuSql = "SELECT product_attribute.name AS name, product_attribute_value.name AS value, "
                                    + " product_attribute_value.sequence AS sequence FROM product_attribute, product_attribute_value, "
                                    + " product_attribute_value_product_product_rel WHERE product_attribute_value_product_product_rel.prod_id = ?"
                                    + " AND product_attribute_value_product_product_rel.att_id = product_attribute_value.id "
                                    + " AND product_attribute_value.attribute_id = product_attribute.id";
                skuStatement = connection.prepareStatement(propSkuSql);
                skuStatement.setInt(1, productId);
                ResultSet propSkuResultSet = skuStatement.executeQuery();
                while (propSkuResultSet.next()) {
                    PropVO propVo = new PropVO();
                    String propName = propSkuResultSet.getString(1);
                    String propValue = propSkuResultSet.getString(2);

                    propVo.setPropName(propName);
                    propVo.setPropValue(propValue);
                    propVo.setSequence(propSkuResultSet.getInt(3));

                    //更新skuPropMap，用来生成skuPropJson
                    if (skuPropMap.containsKey(propName) == false) {
                        skuPropMap.put(propName, new TreeSet<String>());
                    }
                    skuPropMap.get(propName).add(propValue);

                    propVoList.add(propVo);
                }
                skuVo.setPropList(propVoList);

                skuVoList.add(skuVo);
            }

            //在spuVo中设置skuPropJson
            String skuPropJson = JSON.toJSONString(skuPropMap);

            System.out.println("sku attr json = " + skuPropJson);
            spuVo.setSkuPropJson(skuPropJson);

            productVo.setSkuVOList(skuVoList);
            productVo.setSpuVO(spuVo);
	    } catch (SQLException se) {
	        se.printStackTrace();
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            
	            if (productProductStatement != null) {
	                productProductStatement.close();
	            }
	            if (skuStatement != null) {
	                skuStatement.close();
	            }
	        } catch (SQLException se) {
	            se.printStackTrace();
	        }
	    }
        return productVo;
    }
    
    public SpuCateRelation parseProductCategory(ResultSet resultSet, Logger logger){
    	
    	if (resultSet == null){
    		return null;
    	}
    	SpuCateRelation spuCateRelation;
    	
    	spuCateRelation = new SpuCateRelation();
    	
    	try{
	    	int templateId = resultSet.getInt(1);
	    	int publicCateId = resultSet.getInt(2);
	        
	        int spuId = getSpuIdByOldId(templateId);
	        int cateId = getCateIdByOldId(publicCateId);
	        
	        if(spuId != -1 && cateId != -1){
	            spuCateRelation.setSpuId(spuId);
	            spuCateRelation.setCateId(cateId);
	            return spuCateRelation;
	        }else{
	        	return null;
	        }
    	}catch (SQLException e){
    		logger.debug(e.getMessage());
    		return null;
    	}catch(Exception e){
    		logger.debug(e.getMessage());
    		return null;
    	}
    	
    }
    
    private int getSpuIdByOldId(int oldId){
    	if (oldId <= 0){
    		return -1;
    	}
    	Spu spu = null;
    	try{
    		spu = spuDao.selectByOldId(oldId);
    	}catch(TooManyResultsException e){
    		return -1;
    	}
    	
    	if(spu == null){
    		return -1;
    	}else{
    		return spu.getId();
    	}
    }
    
    private int getCateIdByOldId(int oldId){
    	Cate cate = cateDao.selectByOldId(oldId);
    	if (cate == null){
    		return -1;
    	}else{
    		return cate.getId();
    	}
    }

    @Test
    public void transferUser() {
        Statement statement = null;

        try {
            statement = connection112.createStatement();
            String sql = "select * from res_users left join res_partner on res_users.partner_id = res_partner.id ORDER BY res_users.id limit 50 offset 0";
            //            String sql = "select * from res_users left join res_partner on res_users.partner_id = res_partner.id where res_partner.id = 55146";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                User user = new User();
                user.setEmail(resultSet.getString("login"));
                user.setPassword(resultSet.getString("password_crypt"));
                user.setCellphone("");
                user.setNick(resultSet.getString("display_name"));
                user.setOldId(Integer.parseInt(resultSet.getString("id")));
                if (StringUtils.isNotBlank(resultSet.getString("oauth_uid"))
                    && StringUtils.isNotBlank(resultSet.getString("oauth_provider_id"))
                    && StringUtils.isNotBlank(resultSet.getString("oauth_access_token"))) {
                    user.setRegisterType(RegisterType.FACEBOOK.getValue());
                } else {
                    user.setRegisterType(RegisterType.EMAIL.getValue());
                }
                userDao.insert(user);

                UserEtc userEtc = new UserEtc();
                userEtc.setUserId(user.getId());
                userEtc.setPhone(resultSet.getString("phone"));
                String birthdayStr = resultSet.getString("birthdate");
                if (StringUtils.isNotBlank(birthdayStr)) {
                    userEtc.setBirthday(Timestamp.valueOf(birthdayStr));
                }
                String language = resultSet.getString("lang");
                if (StringUtils.isNotBlank(language)) {
                    userEtc.setLanguage(language);
                }
                String countryIdStr = resultSet.getString("country_id");
                if (StringUtils.isNotBlank(countryIdStr)) {
                    userEtc.setCountryId(Integer.parseInt(countryIdStr));
                }
                userEtcDao.insert(userEtc);

                UserOAuth userOAuth = new UserOAuth();
                if (StringUtils.isNotBlank(resultSet.getString("oauth_uid"))
                    && StringUtils.isNotBlank(resultSet.getString("oauth_provider_id"))
                    && StringUtils.isNotBlank(resultSet.getString("oauth_access_token"))) {
                    userOAuth.setOauthUid(resultSet.getString("oauth_uid"));
                    userOAuth.setUserId(user.getId());
                    userOAuth.setOauthProvider(RegisterType.FACEBOOK.name());
                    userOAuthDao.insert(userOAuth);
                }
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void transferOrder() {

        Statement statement = null;

        try {
            statement = connection112.createStatement();
            String sql = "select * from sale_order left join sale_order_line on sale_order.id = sale_order_line.order_id ORDER BY sale_order.id limit 50 offset 0";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Order order = new Order();
                String orderNumberStr = resultSet.getString("id");
                order.setOrderNumber(Long.parseLong(orderNumberStr));
                int userOldId = Integer.parseInt(resultSet.getString("partner_id"));
                order.setUserId(userDao.selectByOldId(userOldId).getId());

                String countryStr = resultSet.getString("shipping_country");
                Country country = null;
                if (StringUtils.isNotBlank(countryStr)) {
                    country = countryDao.selectByName(countryStr);
                }
                CountryState countryState = null;
                String stateStr = resultSet.getString("shipping_state");
                if (StringUtils.isNotBlank(stateStr)) {
                    countryState = countryStateDao.selectByName(stateStr);
                }
                Address address = new Address();
                if (country != null) {
                    address.setCountryId(country.getId());
                }
                if (countryState != null) {
                    address.setStateId(countryState.getId());
                }
                address.setCity(resultSet.getString("shipping_city"));
                address.setStreet(resultSet.getString("shipping_street2"));
                address.setZip(resultSet.getString("shipping_zip"));
                address.setIsDefault(false);
                addressDao.insert(address);
                ShippingInfo shippingInfo = new ShippingInfo();
                shippingInfo.setReceiverName(resultSet.getString("shipping_name"));
                shippingInfo.setReceiverPhone(resultSet.getString("shipping_phone"));
                //TODO
                //                shippingInfo.setReceiverEmail();
                shippingInfo.setAddressId(address.getId());
                //TODO
                //                shippingInfo.setPostFee();
                //                shippingInfo.setDeliveryMethod();
                shippingInfoDao.insert(shippingInfo);

                order.setShippingId(shippingInfo.getId());
                String tradeStatusOdoo = resultSet.getString("state");
                if (StringUtils.isNotBlank(tradeStatusOdoo)) {
                    if (tradeStatusOdoo.equals("draft")) {
                        order.setTradeStatus(TradeStatus.WAIT_FOR_THE_PAYMENT.getStatusCode());
                    } else if (tradeStatusOdoo.equals("sale")) {
                        order.setTradeStatus(TradeStatus.TRADE_SUCCESS.getStatusCode());
                    }
                }
                String amountTotalStr = resultSet.getString("amount_total");
                order.setAmount(Double.parseDouble(amountTotalStr));
                //TODO
                //                order.setCurrency();
                String payTime = resultSet.getString("payment_date");
                if (StringUtils.isNotBlank(payTime)) {
                    order.setGmtPaied(Timestamp.valueOf(payTime));
                    //TODO
                    //                    order.setPayType();
                }
                orderDao.insert(order);

                TradeProduct tradeProduct = new TradeProduct();
                tradeProduct.setOrderNumber(Long.parseLong(orderNumberStr));
                String skuIdStr = resultSet.getString("product_id");
                if (StringUtils.isNotBlank(skuIdStr)) {
                    int skuId = Integer.parseInt(skuIdStr);
                    tradeProduct.setSkuId(skuId);
                    Sku sku = skuDao.select(skuId);
                    if (sku != null) {
                        tradeProduct.setOriginalPrice(sku.getPrice());
                        tradeProduct.setBuyedPrice(sku.getPrice());
                        int spuId = sku.getSpuId();
                        Spu spu = spuDao.select(spuId);
                        if (spu != null) {
                            tradeProduct.setProductName(spu.getName());
                            List<SpuImageRelation> spuImageRelations = spuImageRelationDao
                                .selectBySpuId(spuId);
                            if (spuImageRelations != null && spuImageRelations.size() > 0) {
                                for (SpuImageRelation spuImageRelation : spuImageRelations) {
                                    int imageId = spuImageRelation.getImageId();
                                    Image image = imageDao.select(imageId);
                                    if (image != null && image.getIsMain() == true) {
                                        tradeProduct.setImageUrl(image.getUrl());
                                    }
                                }
                            }
                        }
                    }
                }
                String quantityStr = resultSet.getString("product_uom_qty");
                if (StringUtils.isNotBlank(quantityStr)) {
                    tradeProduct.setQuantity(Integer.parseInt(quantityStr));
                }
                tradeProductDao.insert(tradeProduct);
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
