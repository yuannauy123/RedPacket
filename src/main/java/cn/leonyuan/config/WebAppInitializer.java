package cn.leonyuan.config;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration.Dynamic;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {


	@Override
	protected Class<?>[] getRootConfigClasses() {
		// ����Spring IoC��Դ
		return new Class<?>[] { RootConfig.class };
	}


	@Override
	protected Class<?>[] getServletConfigClasses() {

		return new Class<?>[] { WebConfig.class };
	}

	// DispatchServlet������������
	@Override
	protected String[] getServletMappings() {
		return new String[] { "*.do" };
	}


	@Override
	protected void customizeRegistration(Dynamic dynamic) {

		String filepath = "e:/mvc/uploads";

		Long singleMax = (long) (5 * Math.pow(2, 20));

		Long totalMax = (long) (10 * Math.pow(2, 20));

		dynamic.setMultipartConfig(new MultipartConfigElement(filepath, singleMax, totalMax, 0));
	}

}
