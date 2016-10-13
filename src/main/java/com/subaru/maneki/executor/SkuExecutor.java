package com.subaru.maneki.executor;

import javax.annotation.Resource;

import com.subaru.maneki.exception.ProductException;
import com.subaru.maneki.manager.SkuManager;
import com.subaru.maneki.model.Sku;
import com.subaru.maneki.service.product.SkuService;
import com.subaru.maneki.vo.PropVO;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;

import com.subaru.core.BaseExecute;
import com.subaru.core.encryption.MD5;
import com.subaru.maneki.dao.UserDao;
import com.subaru.maneki.enumeration.RegisterType;
import com.subaru.maneki.model.User;

import java.util.List;

/**
 * @author zhangchaojie
 * @since 2016-08-16
 */
public class SkuExecutor extends BaseExecute {

    @Resource
    private SkuManager skuManager;

    @Resource
    private SkuService skuService;

    @Before
    public void setUp() {

    }

    @Test
    public void addPropsToSku() {
        for (int i=1354; i<=5336; i++){
            System.out.println("Processing sku: " + i + "===================================================================");
            Sku sku = skuService.get(i);
            if (sku == null){
                System.out.println("Cannot get sku by skuId : " + i + ", skip -------------------------------");
                continue;
            }
            List<PropVO> skuPropVOList = skuManager.getSkuPropVOList(i);
            if (skuPropVOList == null){
                System.out.println("Get skuProp with null by skuId : " + i + ", skip -------------------------------");
                continue;
            }
            for (PropVO propVO:skuPropVOList){
                System.out.println("propName: " + propVO.getPropName() + ", propValue: " + propVO.getPropValue());
                if (propVO == null || StringUtils.isBlank(propVO.getPropName()) || StringUtils.isBlank(propVO.getPropValue())){
                    continue;
                }
                switch (propVO.getPropName()){
                    case "Color":
                        sku.setPropColor(propVO.getPropValue());
                        break;
                    case "Size":
                        sku.setPropSize(propVO.getPropValue());
                        break;
                    case "Style":
                        sku.setPropStyle(propVO.getPropValue());
                        break;
                    case "Metal Color":
                        sku.setPropMetalColor(propVO.getPropValue());
                        break;
                    case "cCOLOR":
                        sku.setPropCColor(propVO.getPropValue());
                        break;
                }
                try {
                    skuService.update(sku);
                } catch (ProductException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("End processing sku: " + i + "===================================================================");
        }
    }

}
