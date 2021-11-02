/**
 * 
 */
package com.trumpetinc.boottest.somepackage;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



/**
 * @author Kevin
 *
 */
@Component
public class MyComponent {

	{
		System.out.println("Static init");
	}
	
	@Autowired
	public MyComponent() {
		System.out.println("Class constructor");
		//throw new RuntimeException("Component constructed!");
	}
	
	@PostConstruct
    public void init()  {
		System.out.println("Spring init");
    }
	
}
