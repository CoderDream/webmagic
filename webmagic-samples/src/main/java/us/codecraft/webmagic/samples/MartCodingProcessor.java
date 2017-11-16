package us.codecraft.webmagic.samples;

import java.util.List;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

public class MartCodingProcessor implements PageProcessor {

	private Site site = Site.me().setRetryTimes(10).setSleepTime(1000);

	// 列表页 list_url = "http://www.cnblogs.com/dick159/default.html?page=";

	// private Set<String> done_url = new HashSet<String>();

	// 详情页url = "http://www.cnblogs.com/dick159/p/^\\d{7}$.html";

	@Override
	public void process(Page page) {
		// items clearfix list
		// String xpath =
		// "//div[@id='mart-rewards']/div[@class=content]/div[@class=item-content]";
		//String xpath = "//div[@id='mart-rewards']";

		String xpath = "//*[@id=\"item-11030\"]/div/div[2]/text()";
		String text = page.getHtml().xpath(xpath).toString();
		System.out.println(text);
//		List<String> titleList = page.getHtml().xpath(xpath).all();
//		System.out.println("titleList");
//		for (String string : titleList) {
//			System.out.println(string);
//		}

		// String detail_urls_Xpath =
		// "//*[@class='postTitle']/a[@class='postTitle2']/@href";
		// String next_page_xpath = "//*[@id='nav_next_page']/a/@href";
		// String next_page_css = "#homepage_top_pager > div:nth-child(1) >
		// a:nth-child(7)";
		// String title_xpath = "//h1[@class='postTitle']/a/text()";
		// String date_xpath = "//span[@id='post-date']/text()";
		// page.putField("title", page.getHtml().xpath(title_xpath).toString());
		// if (page.getResultItems().get("title") == null) {
		// page.setSkip(true);
		// }
		// page.putField("date", page.getHtml().xpath(date_xpath).toString());
		//
		// if (page.getHtml().xpath(detail_urls_Xpath).match()) {
		// Selectable detailUrls = page.getHtml().xpath(detail_urls_Xpath);
		// page.addTargetRequests(detailUrls.all());
		// }
		//
		// if (page.getHtml().xpath(next_page_xpath).match()) {
		// Selectable nextPageUrl = page.getHtml().xpath(next_page_xpath);
		// page.addTargetRequests(nextPageUrl.all());
		//
		// } else if (page.getHtml().css(next_page_css).match()) {
		// Selectable nextPageUrl = page.getHtml().css(next_page_css).links();
		// page.addTargetRequests(nextPageUrl.all());
		// }
	}

	@Override
	public Site getSite() {
		return this.site;
	}

	public static void main(String[] args) {
		MartCodingProcessor processor = new MartCodingProcessor();
		String url = "https://mart.coding.net/projects?role_type_id=11";
		// String url =
		// "https://mart.coding.net/projects?type=2&status=&role_type_id=11";
		// String url = "https://mart.coding.net/projects";
		//String url = "https://www.csdn.net/";
		Spider.create(processor).addUrl(url).addPipeline(new TestPipeline())
				.thread(5).run();
	}
}
