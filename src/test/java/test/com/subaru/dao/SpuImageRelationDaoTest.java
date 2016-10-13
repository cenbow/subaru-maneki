package test.com.subaru.dao;

import java.sql.Timestamp;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.subaru.maneki.dao.CateDao;
import com.subaru.maneki.dao.ImageDao;
import com.subaru.maneki.dao.SpuDao;
import com.subaru.maneki.dao.SpuImageRelationDao;
import com.subaru.maneki.model.Image;
import com.subaru.maneki.model.Spu;
import com.subaru.maneki.model.SpuImageRelation;
import com.subaru.core.BaseTestCaseJunit;

public class SpuImageRelationDaoTest extends BaseTestCaseJunit {
    @Resource
    private SpuImageRelationDao spuImageRelationDao;

    @Resource
    private SpuDao              spuDao;

    @Resource
    private CateDao             cateDao;

    @Resource
    private ImageDao            imageDao;

    private SpuImageRelation    spuImage;

    private Spu                 spu;

    private Image               image;

    @Before
    public void setUp() {
        spu = new Spu();
        spu.setCateId(1);
        spu.setClusterId(1123234);

        spu.setIsPublished(true);
        spu.setName("Twist Sheath Slim Gray O Neck Casual Knitted Sweater Dress");
        spu.setProductNo("CBS000101633N");
        spu.setScore("5.0/5.0");
        spu.setOldId(1230);
        spu.setSearchKeywords("High Quality Women Twist Sweater Dress Autumn Winter New Arrival Fashion Sheath Slim Gray O Neck Casual Knitted Sweater Dress");
        spu.setComment("[{\"buyer\": \"Elena A.\", \"rating\": \"4.0\", \"images\": "
                       + "[{\"s\": \"http://g02.a.alicdn.com/kf/UT8qYj1XsxaXXagOFbXz.jpg_50x50.jpg\", \"b\": \"http://g02.a.alicdn.com/kf/UT8qYj1XsxaXXagOFbXz.jpg_350x350.jpg\"},"
                       + " {\"s\": \"http://g04.a.alicdn.com/kf/UT8zYv0XEdaXXagOFbXL.jpg_50x50.jpg\", {\"buyer\": \"A***a J.\", \"rating\": \"5.0\", \"images\": "
                       + "[], \"feedback\": \"The bike is good. very soft. I recommend at this price\", \"time\": \"15 Apr 2016 05:10\"}]");
        spuDao.insert(spu);

        String url = "www.baidu.com";
        String smallUrl = url + "_20-20.jpg";
        String middleUrl = url + "_100-100.jpg";
        int sequence = 40;

        image = new Image(url, smallUrl, middleUrl, sequence);
        imageDao.insert(image);

        //        System.out.println("spu id = "+spu.getId() + "image id = " + image.getId());

        spuImage = new SpuImageRelation(spu.getId(), image.getId());
        spuImageRelationDao.insert(spuImage);
    }

    @Test
    public void testSelect() {
        SpuImageRelation sir = spuImageRelationDao.select(spu.getId(), image.getId());

        Assert.assertNotNull(sir);

        Image newImage = imageDao.select(sir.getImageId());
        Assert.assertTrue(newImage.getUrl().equals(image.getUrl()));
        Spu newSpu = spuDao.select(spu.getId());
        Assert.assertTrue(newSpu.getIsPublished() == spu.getIsPublished());
        Assert.assertTrue(newSpu.getComment().equals(spu.getComment()));
    }

    @Test
    public void testUpdate() {
        SpuImageRelation sir = spuImageRelationDao.select(spu.getId(), image.getId());

        Timestamp oldUpdateTime = sir.getGmtUpdate();

        //等待两秒后再插入
        try {
            Thread.sleep(1000 * 2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String url = "www.google.com";
        String smallUrl = url + "_20-20.jpg";
        String middleUrl = url + "_100-100.jpg";
        Image newImage = new Image(url, smallUrl, middleUrl);
        imageDao.insert(newImage);

        //SpuImageRelation newSpuImage = new SpuImageRelation(spu.getId(), newImage.getId());

        spuImageRelationDao.update(spu.getId(), newImage.getId(), sir.getSpuId(), image.getId());

        sir = spuImageRelationDao.select(spu.getId(), newImage.getId());

        Timestamp newUpdateTime = sir.getGmtUpdate();

        Assert.assertFalse(oldUpdateTime.equals(newUpdateTime));
    }
}
