/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {expect, test} from '@playwright/test';

import dragAndDrop from '../utils/dragAndDrop';
import performLogin from '../utils/performLogin';

test('Test Liferay Sample Custom Element 2', async ({page}) => {
	await page.goto('/');

	await expect(page.getByText('Welcome to Liferay')).toBeVisible();

	const login = await page.getByRole('button', { name: 'Sign In' }).isVisible()

	if (login) {
		await performLogin(page, 'test');
	}

	await page.getByRole('link', {name: 'Edit'}).click();
	await page.getByRole('tab', {exact: true, name: 'Widgets'}).click();

	const menuitem = page.getByRole('menuitem', {
		name: 'Liferay Sample Custom Element 2',
	});

	const target = page
		.locator('.page-editor__container')
		.first();

	await dragAndDrop(menuitem, target, {
		sourcePosition: {
			x: 20,
			y: 20,
		},
		targetPosition: {
			x: 20,
			y: 20,
		},
	});

	await expect(page.getByText('Hello Test')).toBeVisible();

	await page.getByLabel('Publish', {exact: true}).click();

	await expect(page.getByText('Hello Test')).toBeVisible();

	await expect(page.getByTestId('custom-element-2-comic')).toBeVisible();

	await expect(page.getByTestId('custom-element-2-dad-joke')).toBeVisible();
});
