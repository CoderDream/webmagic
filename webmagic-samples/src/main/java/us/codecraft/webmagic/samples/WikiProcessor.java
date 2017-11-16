package us.codecraft.webmagic.samples;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

public class WikiProcessor implements PageProcessor {

	private Site site = Site.me().setRetryTimes(10).setSleepTime(1000);

	// 列表页 list_url = "http://www.cnblogs.com/dick159/default.html?page=";

	// private Set<String> done_url = new HashSet<String>();

	// 详情页url = "http://www.cnblogs.com/dick159/p/^\\d{7}$.html";

	private Integer cntx;

	private Integer cnty;

	private Integer cnt;

	@Override
	public void process(Page page) {

		if (page.getHtml().toString().contains("消歧")) {
			// System.out.println("1" + page.getUrl().toString());
			Document doc = Jsoup.parse(page.getHtml().toString());
			Element ele = doc.select("div.mw-content-ltr").first();
			Element ul = ele.getElementsByTag("ul").first();
			page.putField("addr", ul.text());

			System.out.println("1:" + cntx++);
		}

		// else if(page.getHtml().xpath("//div[@id='noarticletext']/p/b/text()")
		// != null &&
		// page.getHtml().xpath("//div[@id='noarticletext']/p/b/text()").toString().contains("没有")){
		else if (page.getHtml().toString().contains("维基百科目前还没有与上述标题相同的条目")) {
			System.out.println("2" + page.getUrl().toString());
			System.out.println("2:" + cnty++);
		} else {

			String th = "";
			String td = "";
			String test = "";
			// System.out.println("3" + page.getUrl().toString());
			Document doc = Jsoup.parse(page.getHtml().toString());
			Element ele = doc.select("div.mw-content-ltr").first();

			Element tbody = ele.getElementsByClass("infobox").first()
					.getElementsByTag("tbody").first();

			for (int i = 1; i < 15; i++) {
				// th = page.getHtml().xpath("//table[@class=infobox]/tbody/tr["
				// + i + "]/th//text()").toString();
				// td = page.getHtml().xpath("//table[@class=infobox]/tbody/tr["
				// + i + "]/td//text()").toString();
				if (tbody.children().size() <= i)
					break;
				Element thx = tbody.child(i).child(0);
				th = thx.text();
				// System.out.println(tbody);
				if (tbody.child(i).children().size() > 1) {
					Element tdx = tbody.child(i).child(1);
					td = tdx.text();
					// System.out.println(thx.text());
				}
				if (th != null && td != null && th != "" && td != "") {

					// page.putField(th, td);
					// test += th + ":" + td + "\n";
					page.putField(th, td);
					// System.out.println(th + ":" + td);

				}
				// System.out.println("done");
				th = "";
				td = "";
			}

			// writer.write(th + ":" + td + "\n");

			System.out.println("3:" + cnt++);
		}
	}

	@Override
	public Site getSite() {
		return this.site;
	}

	public static void main(String[] args) {
		WikiProcessor processor = new WikiProcessor();
		String url = "http://zh.wikipedia.org/zh-cn/剑山";
		// String url = "http://www.cnblogs.com/dick159/default.html?page=1";
		Spider.create(processor).addUrl(url).addPipeline(new TestPipeline())
				.thread(5).run();
	}
}
