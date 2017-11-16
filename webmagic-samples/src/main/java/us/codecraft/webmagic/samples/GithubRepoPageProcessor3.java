package us.codecraft.webmagic.samples;

import java.util.List;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * @author code4crafter@gmail.com <br>
 * @since 0.5.1
 */
public class GithubRepoPageProcessor3 implements PageProcessor {

	// 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
	private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

	@Override
	// process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
	public void process(Page page) {
		// \?l=java.*
		List<String> urls = page.getHtml().css("div.pagination").links().regex(".*/search/\\?l=java.*").all();
		page.addTargetRequests(urls);
		
//		// 部分二：定义如何抽取页面信息，并保存下来
//		String str0 = page.getUrl()
//		.regex("https://github\\.com/(\\w+)/.*").toString();
//		page.putField("author", page.getUrl()
//				.regex("https://github\\.com/(\\w+)/.*").toString());
//
//		String str = page.getHtml().xpath(
//				"//h1[@class='entry-title public']/strong/a/text()")
//				.toString();
//		page.putField("name",
//				page.getHtml().xpath(
//						"//h1[@class='entry-title public']/strong/a/text()")
//						.toString());
//		if (page.getResultItems().get("name") == null) {
//			// skip this page
//			page.setSkip(true);
//		}
//		page.putField("readme",
//				page.getHtml().xpath("//div[@id='readme']/tidyText()"));
//
//		// 部分三：从页面发现后续的url地址来抓取
//		page.addTargetRequests(page.getHtml().links()
//				.regex("(https://github\\.com/[\\w\\-]+/[\\w\\-]+)").all());
	}

	@Override
	public Site getSite() {
		return site;
	}

	public static void main(String[] args) {
		String url = "https://github.com/search?l=Java&p=1&q=stars%3A%3E1&s=stars&type=Repositories";
		Spider.create(new GithubRepoPageProcessor3())
				// 从"https://github.com/code4craft"开始抓
				.addUrl(url)
				// 开启5个线程抓取
				.thread(5)
				// 启动爬虫
				.run();
	}
}