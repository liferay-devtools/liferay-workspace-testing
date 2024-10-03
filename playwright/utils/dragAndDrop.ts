/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {Locator, expect} from '@playwright/test';

async function dragAndDrop(from: Locator, to: Locator, options: any = {}) {
	await expect(from).toBeVisible();
	await expect(to).toBeVisible();

	await from.dragTo(to, {...options, trial: true});
	await from.dragTo(to, options);
}

export default dragAndDrop;
