/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {Cookie, Page, expect} from '@playwright/test';

const userData = {
	'test': {
		initialPassword: 'test',
		name: 'Test',
		password: "test1",
		surname: 'Test',
	},
};

export type LoginScreenName = keyof typeof userData;

async function performLogin(
	page: Page,
	screenName: LoginScreenName
): Promise<Cookie[]> {
	const {initialPassword, name, password, surname} = userData[screenName];

	await page.goto('/');

	await page.getByRole('button', {name: 'Sign In'}).click();

	await page.getByLabel('Email Address').fill(`${screenName}@liferay.com`);
	await page.getByLabel('Password').fill(password);
	await page.getByLabel('Remember Me').check();

	await page
		.getByLabel('Sign In- Loading')
		.getByRole('button', {name: 'Sign In'})
		.click();

	await page.waitForNavigation();

	const wrongPassword = await page.getByText('Authentication failed due to incorrect credentials').isVisible()

	if (wrongPassword) {
		await page.getByLabel('Email Address').fill(`${screenName}@liferay.com`);
		await page.getByLabel('Password').fill(initialPassword);
		await page.getByLabel('Remember Me').check();

		await page
			.getByRole('button', {name: 'Sign In'})
			.click();

		await page.waitForNavigation();
	}

	const changePassword = await page.getByRole('heading', { name: 'Change Password' }).isVisible()

	if (changePassword) {
		await page.getByLabel('Password', {exact: true}).fill(password);
		await page.getByLabel('Reenter Password', {exact: true}).fill(password);

		await page
			.getByRole('button', { name: 'Save' })
			.click();
	}

	await expect(
		page.getByLabel(`${name} ${surname} User Profile`)
	).toBeVisible({
		timeout: 30 * 1000,
	});

	return await page.context().cookies();
}

export default performLogin;
