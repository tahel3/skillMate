import { configureStore } from "@reduxjs/toolkit";
import { api } from "../api/Api"; 
import authReducer from "../features/AuthSlice"; 

export const store = configureStore({
  reducer: {
    [api.reducerPath]: api.reducer,
    auth: authReducer, 
  },
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware().concat(api.middleware),
});