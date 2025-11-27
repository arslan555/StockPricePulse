package com.arslan.stockpricepulse.ui.screens.pricetracker

/**
 * Represents side effects in the MVI pattern.
 * Side effects are one-time events that should be handled by the UI (e.g., showing a snackbar, navigation).
 * Unlike state, side effects are consumed once and not persisted.
 */
sealed interface PriceTrackerSideEffect {
    /**
     * Show an error message to the user (e.g., via Snackbar).
     *
     * @param message The error message to display
     */
    data class ShowError(val message: String) : PriceTrackerSideEffect

    /**
     * Show a success message to the user (e.g., via Snackbar).
     *
     * @param message The success message to display
     */
    data class ShowSuccess(val message: String) : PriceTrackerSideEffect

    /**
     * Show an informational message to the user (e.g., via Snackbar).
     *
     * @param message The informational message to display
     */
    data class ShowMessage(val message: String) : PriceTrackerSideEffect
}

