import { HttpErrorResponse } from '@angular/common/http';
import { ErrorHandler, Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root'
})
export class ErrorHandlerService implements ErrorHandler {

  constructor(private snackBar: MatSnackBar) {} 

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
