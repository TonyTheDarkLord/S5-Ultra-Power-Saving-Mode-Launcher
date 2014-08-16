package com.spacewizz.powersavelauncher;

public class SortItems {
	public void exchange_sort(MainActivity.Pac[] pacs) {
		int i, j;
		MainActivity.Pac temp;

		for (i = 0; i < pacs.length - 1; i++) {
			for (j = i + 1; j < pacs.length; j++) {
				if (pacs[i].label.compareToIgnoreCase(pacs[j].label) > 0) { // ascending
																			// sort
					temp = pacs[i];
					pacs[i] = pacs[j]; // swapping
					pacs[j] = temp;

				}
			}
		}
	}
}