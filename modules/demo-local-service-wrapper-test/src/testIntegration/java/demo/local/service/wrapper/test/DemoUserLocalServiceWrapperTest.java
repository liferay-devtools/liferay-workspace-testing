package demo.local.service.wrapper.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceWrapper;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Drew Brokke
 */
@RunWith(Arquillian.class)
public class DemoUserLocalServiceWrapperTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Test
	public void testGetUser() throws Exception {
		_log.info(
			String.format(
				"[%s] Running testGetUser integration test...",
				DemoUserLocalServiceWrapperTest.class.getName()));

		User user1 = UserTestUtil.addUser();

		User user2 = _userLocalService.getUser(user1.getUserId());

		Assert.assertEquals(
			"Comments should be modified by the wrapper",
			"Hello DemoUserLocalServiceWrapper! userId: " + user1.getUserId(),
			user2.getComments());
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DemoUserLocalServiceWrapperTest.class);

	@Inject(
		filter = "component.name=demo.local.service.wrapper.DemoUserLocalServiceWrapper"
	)
	private ServiceWrapper<?> _demoUserLocalServiceWrapper;

	@Inject
	private UserLocalService _userLocalService;

}