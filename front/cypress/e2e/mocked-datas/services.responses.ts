export const ADMIN_LOGIN = {
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ5b2dhQHN0dWRpby5jb20iLCJpYXQiOjE3MTk0NzA3NTEsImV4cCI6MTcxOTU1NzE1MX0.f8LeF1JMeKa0wkzkVtD1TruOxiDeQk0rP7dqJ_8ynxHbzhAvysCoX6tdOxYtTt4nv8LOpoQMNw9JLUvsWYzN8A",
  "type": "Bearer",
  "id":1,
  "username":"yoga@studio.com",
  "lastName": "Admin",
  "firstName": "Admin",
  "admin": true,
};

export const CHRISTINA_LOGIN = {
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ5b2dhQHN0dWRpby5jb20iLCJpYXQiOjE3MTk0NzA3NTEsImV4cCI6MTcxOTU1NzE1MX0.f8LeF1JMeKa0wkzkVtD1TruOxiDeQk0rP7dqJ_8ynxHbzhAvysCoX6tdOxYtTt4nv8LOpoQMNw9JLUvsWYzN8A",
  "type": "Bearer",
  "id":2,
  "username": "caguilera@studio.com",
  "firstName": "Christina",
  "lastName": "Aguilera",
  "admin": false,
};

export const LOGIN_INVALID = {
  "error":"Unauthorized",
  "message":"Bad credentials",
  "status":401
}

export const ADMIN_USER = {
  "id": 1,
  "email": "yoga@studio.com",
  "lastName": "Admin",
  "firstName": "Admin",
  "admin": true,
  "createdAt": "2024-04-19T18:22:28",
  "updatedAt": "2024-04-19T18:22:28"
};

export const CHRISTINA_USER = {
  "id": 2,
  "email": "caguilera@studio.com",
  "firstName": "Christina",
  "lastName": "Aguilera",
  "admin": false,
  "createdAt": "2024-04-19T18:22:28",
  "updatedAt": "2024-04-19T18:22:28"
}

export const CHRISTINA_TEACHER = {
  "id": 1,
  "lastName": 'Aguilera',
  "firstName": 'Christina',
  "createdAt":"2024-04-24T17:06:29",
  "updatedAt":"2024-04-24T17:06:29"
};

export const TEACHER2 = {
  "id": 2,
  "lastName": 'Peel',
  "firstName": 'William',
  "createdAt":"2024-04-24T17:06:29",
  "updatedAt":"2024-04-24T17:06:29"
};

export const CHRISTINA_SESSION =   {
    "id":1,
    "name":"Yog' Aguilera",
    "date":"2024-08-05T00:00:00.000+00:00",
    "teacher_id":2,
    "description":"Yoga with Christina",
    "users":[1, 3],
    "createdAt":"2024-04-24T17:06:29",
    "updatedAt":"2024-04-24T17:06:29"
};

export const SESSION2 = {
  "id":2,
  "name":"Peel Session",
  "date":"2020-07-03T00:00:00.000+00:00",
  "teacher_id":3,
  "description":"Celebs William Peel Session",
  "users":[1, 2, 3],
  "createdAt":"2024-04-24T17:15:10",
  "updatedAt":"2024-04-24T17:15:10"
};

export const SESSIONS = [CHRISTINA_SESSION, SESSION2];

