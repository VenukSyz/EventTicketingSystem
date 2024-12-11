/**
 * Represents a generic API response model.
 * 
 * The `data` property holds the response data from the API.
 * This is a flexible model that can be used to represent various types of response data
 * depending on the specific API endpoint and use case.
 */
export interface IApiResponseModel{
    /** The data returned from the API response. */
    data: any;
}