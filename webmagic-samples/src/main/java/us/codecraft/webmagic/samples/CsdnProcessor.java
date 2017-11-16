package us.codecraft.webmagic.samples;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

/**
 * csdn 爬取控制
 */
public class CsdnProcessor implements PageProcessor {

	public static Logger logger = LoggerFactory.getLogger(CsdnProcessor.class);

	private Site site = Site.me().setSleepTime(0).setCycleRetryTimes(3);

	/**
	 * csdn uri 后缀
	 */
	public static final String CSDN_URI = "xuxiheng";

	@Override
	public void process(Page page) {
		// http://blog.csdn.net/xuxiheng/article/list/1
		// http://blog.csdn.net/xuxiheng/article/list/2
		// http://blog.csdn.net/xuxiheng/article/list/\\d*
		// 使用正则表达式，添加所有页
		List<String> pagenation = page.getHtml().links()
				.regex("/" + CSDN_URI + "/article/list/\\d*").all();
		page.addTargetRequests(pagenation);
		// 文章列表页面只捕捉每篇文章url，并将其加入爬取队列
		if (CollectionUtils.isNotEmpty(pagenation)) {
			// //*[@id="article_list"]/div[20]/div[1]/h1/span/a
			List<String> titleList = page.getHtml()
					.xpath("//div[@id='article_list']/div[@class=list_item]")
					.all();
			for (String titleHtml : titleList) {
				page.addTargetRequests(new Html(titleHtml).links()
						.regex("/" + CSDN_URI + "/article/details/\\d*").all());
			}
			page.setSkip(true);
		} else { // csdn具体文章页面
			//String titleXpath = "html/body/div[5]/main/article/h1";
			// String xpath0 = "//div[@class=article_title]/h1/span/a/text()";
			
			//String titleXpath = "/html/body/div[4]/main/article/h1/text()";
			String titleXpath = "//[@class='csdn_top']/text()";
			//String title_xpath = "//*[@id=\"cb_post_title_url\"]/text()";
			String title = page.getHtml().xpath(titleXpath).toString();
			
			//String titleCss = "csdn_top";
			//String title = page.getHtml().css(titleCss).toString();
			
			//String createTimeXpath = "html/body/div[5]/main/article/div[1]/div/span[2]";
			String createTimeXpath = "//[@class='time']/text()";
			// "//div[@class=article_r]/span[@class=link_postdate]/text()"
			String createTime = page.getHtml().xpath(createTimeXpath)
					.toString();
			page.putField("title", title);
			page.putField("createTime", createTime);
		}
	}

	@Override
	public Site getSite() {
		return site;
	}

	// public static Logger logger = LoggerFactory.getLogger(CsdnMain.class);

	public static void main(String[] args) {

		Spider.create(new CsdnProcessor())
				// 从url开始抓
				.addUrl("http://blog.csdn.net/" + CsdnProcessor.CSDN_URI)
				// 设置Scheduler，使用File来管理URL队列
				// .setScheduler(new
				// FileCacheQueueScheduler("/Users/zhengyong/queue"))
				// 设置Pipeline，将结果以console方式输出到控制台
				.addPipeline(new ConsolePipeline())
				// 开启5个线程同时执行
				.thread(5)
				// 启动爬虫
				.run();
	}

}