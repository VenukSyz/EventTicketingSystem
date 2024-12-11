import { HttpErrorResponse } from '@angular/common/http';
import { ErrorHandler, Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root'
})
export class ErrorHandlerService implements ErrorHandler {

  /**
   * Creates an instance of ErrorHandlerService.
   * @param snackBar - The MatSnackBar instance used for displaying error messages.
   */
  constructor(private snackBar: MatSnackBar) {} 

  /**
   * Handles the error and displays an appropriate message to the user.
   * This method is invoked whenever an error occurs in the application.
   * It categorizes errors into HTTP, client-side, TypeError, ReferenceError, or general errors.
   * It also logs the error and shows the error message in a snack bar.
   * 
   * @param error - The error object that occurred. Can be an instance of HttpErrorResponse, ErrorEvent, TypeError, ReferenceError, or any other error.
   */
  handleError(error: any): void {
    let errorMessage = "An unknown error occurred";

    if (error instanceof HttpErrorResponse) {
      errorMessage = `Server Error: ${error.status} - ${error.message}`;
    } else if (error.error instanceof ErrorEvent) {
      // Client-side error
      errorMessage = `Error: ${error.error.message}`;
    } else if (error instanceof TypeError) {
      // Handle TypeErrors
      errorMessage = `Type Error: ${error.message}`;
    } else if (error instanceof ReferenceError) {
      // Handle ReferenceErrors
      errorMessage = `Reference Error: ${error.message}`;
    } else {
      // Generic error for any other cases
      errorMessage = `Unexpected Error: ${error.toString()}`;
    }

    // Log the error (could be sent to a logging server)
    console.error(errorMessage);

    // Display error to the user
    this.snackBar.open(errorMessage, 'Dismiss', { duration: 0, horizontalPosition: 'right',
      verticalPosition: 'top', });
  }
}
