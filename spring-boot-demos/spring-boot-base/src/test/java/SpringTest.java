import java.lang.reflect.Method;
import java.util.List;

import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.TypedValue;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.ReflectionUtils;

/**
 *@author : wh
 *@date : 2023/11/8 15:42
 *@description:
 */
public class SpringTest {

	/**
	 * 测试 spring el 表达式
	 */
	@org.junit.jupiter.api.Test
	public void spelExpressionParserTest() {
		SpelExpressionParser parser = new SpelExpressionParser();
		Expression expression = parser.parseExpression("name");
		Person person = new Person("xiao zou");
		String result = expression.getValue(person, String.class);
		System.out.println(result);
		Assertions.assertEquals("xiao zou", result);

	}

	/**
	 * 测试 srping el表达式解析常量
	 */
	@Test
	public void testSpelExpressionParser() {
		SpelExpressionParser parser = new SpelExpressionParser();
		String value = parser.parseExpression("'xiaozou'").getValue(String.class);//  ,注意string有单引号包裹
		Long value1 = parser.parseExpression("1.024E+3").getValue(Long.class);// 1024  , 指数形式
		Integer value2 = parser.parseExpression("0x208").getValue(Integer.class);// 520 十六进制  0x208
		Boolean value3 = parser.parseExpression("true").getValue(Boolean.class);// true
		Object value4 = parser.parseExpression("null").getValue();
		System.out.printf("value: %s\n value1: %s\n value2: %s\n value3: %s\n value4: %s\n", value, value1, value2, value3, value4);
	}
	

	/**
	 * 上下文
	 */
	@org.junit.jupiter.api.Test
	public void testStandardEvaluationContext() {
		// 创建 SpelExpressionParser 实例
		SpelExpressionParser parser = new SpelExpressionParser();
		// 创建 StandardEvaluationContext 实例
		StandardEvaluationContext context = new StandardEvaluationContext();

		// 在上下文中设置变量
		context.setVariable("name", "xiaozou");
		context.setVariable("age", 18);

		// 解析表达式并计算结果
		Expression expression = parser.parseExpression("'Hello, ' + #name + '! You are ' + #age + ' years old.'");
		String result = expression.getValue(context, String.class);
		// 输出结果
		System.out.println(result);

		ParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();
		// MethodBasedEvaluationContext
		MethodBasedEvaluationContext methodBasedEvaluationContext = new MethodBasedEvaluationContext(TypedValue.NULL, ReflectionUtils.findMethod(InnerClass.class, "method", List.class), null, discoverer);
		
	}

	/**
	 * 测试 ParameterNameDiscoverer 解析方法参数解
	 */
	@org.junit.jupiter.api.Test
	public void testStandardReflectionParameterNameDiscoverer() {
		ParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();
		// discoverer = new LocalVariableTableParameterNameDiscoverer();
		// discoverer = new DefaultParameterNameDiscoverer();

		Method method = ReflectionUtils.findMethod(InnerClass.class, "method", List.class);
		String[] actualParams = discoverer.getParameterNames(method);
		Assertions.assertEquals(actualParams[0], "users");
	}

	public class InnerClass {
		
		public void method(List<String> users) {
		}
	}

	@Data
	public static class Person {
		private String name;

		public Person(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

	/**
	 * 测试 spring el表达式解析属性获取值
	 */
	@Test
	public void spelExampleGetProperties() {
		ExpressionParser parser = new SpelExpressionParser();
		Expression expression = parser.parseExpression("name");
		Person person = new Person("xiaozou");
		String result = expression.getValue(person, String.class);
		System.out.println(result); // Output: xiaozou
	}

	/**
	 * 测试 srping el表达式解析执行方法
	 */
	@Test
	public void spelExampleMethod() {
		ExpressionParser parser = new SpelExpressionParser();
		Expression expression = parser.parseExpression("toUpperCase()");
		String str = "xiaozou";
		String result = expression.getValue(str, String.class);
		System.out.println(result); // Output: XIAOZOU
	}

	/**
	 * 测试 spring el 从context 解析对象属性
	 */
	@org.junit.Test
	public void testELObject() {
		ExpressionParser parser = new SpelExpressionParser();
		Expression expression = parser.parseExpression("name");
		Person person = new Person("xiao zou");
		StandardEvaluationContext context = new StandardEvaluationContext(person);
		String result = expression.getValue(context, String.class);
		System.out.println(result); // Output: xiaozou
	}
	
	@org.junit.Test
	public void test() throws Exception {
		ExpressionParser parser = new SpelExpressionParser();
		Expression expression = parser.parseExpression("reverse('Hello')");

		StandardEvaluationContext context = new StandardEvaluationContext();
		context.registerFunction("reverse", StringUtils.class.getDeclaredMethod("reverse", String.class));

		String result = expression.getValue(context, String.class);
		System.out.println(result); // Output: olleH
		
	}

	public static class StringUtils {
		public static String reverse(String str) {
			return new StringBuilder(str).reverse().toString();
		}
	}
	
	@org.junit.Test
	public void test11() throws Exception{
		ParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();
		SpelExpressionParser parser = new SpelExpressionParser();
		StandardEvaluationContext context = new StandardEvaluationContext();
		// 获取方法参数名
		Method method = MyClass.class.getMethod("myMethod", String.class, int.class);
		String[] parameterNames = discoverer.getParameterNames(method);

		for (String parameterName : parameterNames) {
			System.out.println("parameterName " + parameterName);
		}

	}
	
	@Test
	public void testParameterNameDiscoverer() throws Exception{
		ParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();
		Method method = MyClass.class.getMethod("myMethod", String.class, int.class);
		String[] parameterNames = discoverer.getParameterNames(method);
		for (String parameterName : parameterNames) {
			System.out.println("parameterName " + parameterName);// out  parameterName name parameterName age
		}
	}
	
	@Data
	static class MyClass {
		private String name = "Default";
		private int age = 0;
		public void myMethod(String name, int age) {
			// Method body
		}
	}
	
}
