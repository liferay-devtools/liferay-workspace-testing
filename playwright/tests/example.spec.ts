/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {Locator, expect, test} from '@playwright/test';

import performLogin from '../utils/performLogin.ts';

async function dragAndDrop(from: Locator, to: Locator, options: any = {}) {
	await expect(from).toBeVisible();
	await expect(to).toBeVisible();

	await from.dragTo(to, {...options, trial: true});
	await from.dragTo(to, options);
}

test('Test Liferay Sample Custom Element 1', async ({page}) => {
	await page.goto('/');

	await expect(page.getByText('Welcome to Liferay')).toBeVisible();

	const login = await page.getByRole('button', { name: 'Sign In' }).isVisible()

	if (login) {
		await performLogin(page, 'test');
	}

	await page.getByRole('link', {name: 'Edit'}).click();
	await page.getByRole('tab', {exact: true, name: 'Widgets'}).click();

	const menuitem = page.getByRole('menuitem', {
		name: 'Liferay Sample Custom Element 1',
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

	await expect(page.getByText('Portlet internal route:')).toBeVisible();

	await page.getByLabel('Publish', {exact: true}).click();

	await expect(page.getByText('Portlet internal route:')).toBeVisible();

	const vanillaCounterWidget = page.locator('vanilla-counter');

	await expect(vanillaCounterWidget).toBeVisible();

	await expect(vanillaCounterWidget.getByText('0')).toBeVisible();

	const plusButton = vanillaCounterWidget.getByRole('button', {
		exact: true,
		name: '+',
	});

	await plusButton.click();
	await plusButton.click();

	await expect(vanillaCounterWidget.getByText('1')).not.toBeVisible();
	await expect(vanillaCounterWidget.getByText('2')).toBeVisible();

	const minusButton = vanillaCounterWidget.getByRole('button', {
		exact: true,
		name: '-',
	});

	await minusButton.click();

	await expect(vanillaCounterWidget.getByText('1')).toBeVisible();
	await expect(vanillaCounterWidget.getByText('2')).not.toBeVisible();
});
