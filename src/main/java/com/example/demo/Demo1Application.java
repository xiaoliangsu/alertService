package com.example.demo;

import config.GlobalCorsConfig;
import controller.ALertController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RestController;
import utils.DataManager;

@RestController
@SpringBootApplication
@EnableAutoConfiguration
@Import(GlobalCorsConfig.class)
public class Demo1Application implements EmbeddedServletContainerCustomizer{

	@Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        
        container.setPort(8006);
    }

	public static void main(String[] args) {
		SpringApplication.run(ALertController.class, args);
		DataManager.getInstance().startLoading();
//		SpringApplication.run(AddDeviceController.class, args);
//
//		SpringApplication.run(MeasureController.class, args);
//
//		SpringApplication.run(DeviceDetailController.class, args);

	}
}
