# Technology Stack Analysis for Twelvet Project

This document provides an overview of the technology stack, core components, and modular structure of the Twelvet project.

## 1. Core Frameworks and Libraries

The project is built using a modern Java-based microservices architecture. Key technologies include:

| Technology                               | Version         | Purpose                                                                                                                                    |
| :--------------------------------------- | :-------------- | :----------------------------------------------------------------------------------------------------------------------------------------- |
| **Java**                                 | 17              | Core programming language.                                                                                                                 |
| **Spring Boot**                          | 3.4.5           | Primary application framework for building microservices. Simplifies dependency management and auto-configuration.                         |
| **Spring Cloud**                         | 2024.0.1        | Provides tools for building distributed systems (e.g., service discovery, configuration, circuit breakers, routing).                       |
| **Spring Cloud Alibaba**                 | 2023.0.3.2      | Enhances Spring Cloud with Alibaba's microservice solutions, including Nacos for service discovery/configuration and Sentinel for resilience. |
| **MyBatis**                              | 3.0.4           | Persistence framework for simplified database interaction using SQL.                                                                       |
| **Nacos**                                | (Implied)       | Service discovery, configuration management, and service governance for microservices.                                                     |
| **Redis (via Redisson)**                 | 3.41.0          | In-memory data store used for caching, distributed locks, session management, etc. Redisson provides a rich Java client.                     |
| **Spring Boot Admin**                    | 3.4.5           | For monitoring and managing Spring Boot applications.                                                                                      |
| **Docker**                               | (Project setup) | Containerization platform for packaging, deploying, and running applications in isolated environments.                                     |
| **Maven**                                | (Project setup) | Build automation and dependency management tool.                                                                                           |
| **Spring AI / Alibaba Cloud AI**         | 1.0.0 / M6.1    | Integration with Artificial Intelligence capabilities, potentially leveraging Alibaba Cloud's AI services.                                   |
| **API Documentation (Swagger/Springdoc/Knife4j)** | Various         | Tools for generating and visualizing RESTful API documentation (Springdoc for OpenAPI 3, Knife4j for enhanced UI).                     |
| **Sentinel**                             | (Implied)       | A flow control and circuit breaking component for ensuring application resilience and stability.                                           |

## 2. Other Notable Libraries

| Library                                  | Version         | Purpose                                                                                             |
| :--------------------------------------- | :-------------- | :-------------------------------------------------------------------------------------------------- |
| **Dynamic DataSource / ShardingSphere**  | 4.3.1 / 5.4.1   | Advanced database management, potentially for multiple data sources or data sharding.                 |
| **PageHelper**                           | 2.1.0           | MyBatis pagination plugin to simplify query pagination.                                               |
| **Hutool**                               | 5.8.36          | A comprehensive Java utility library.                                                               |
| **XXL-Job**                              | 3.1.0           | A distributed task scheduling framework for managing and executing scheduled jobs.                  |
| **Forest**                               | 1.6.4           | A declarative HTTP client framework, simplifying calls to remote HTTP services.                     |
| **JavaCV**                               | 1.5.11          | Java interface to computer vision libraries (e.g., OpenCV, FFmpeg), likely for image/video processing. |
| **JustAuth**                             | 1.16.7          | Simplifies integration with third-party OAuth2 providers for social logins.                         |
| **Commons IO**                           | 2.18.0          | Utility library for I/O operations.                                                                 |
| **Jasypt**                               | 3.0.5           | Library for encrypting sensitive information in configuration files.                                  |
| **Excel Starter (com.pig4cloud.excel)**  | 3.4.0           | Simplifies Excel file import and export operations.                                                   |
| **XSS Starter (net.dreamlu.mica-xss)**   | 3.1.5.1         | Provides protection against Cross-Site Scripting (XSS) attacks.                                       |
| **OSS Starter (com.pig4cloud.plugin)**   | 3.0.0           | Generic Object Storage Service (OSS) integration, likely for services like MinIO or Aliyun OSS.       |
| **Idempotent Starter (com.pig4cloud.plugin)**| 0.4.0       | Helps in designing idempotent operations in APIs.                                                     |
| **Sensitive Word (com.github.houbb)**    | 0.22.0          | Library for filtering sensitive words.                                                              |
| **Yitter IDGenerator**                   | 1.0.6           | Distributed unique ID generator (Snowflake algorithm variant).                                      |

## 3. Project Modular Structure

The Twelvet project follows a highly modular, microservices-oriented architecture. This structure promotes separation of concerns, scalability, and maintainability. The main components are:

*   **`twelvet` (Parent POM):**
    *   The root Maven project that defines shared configurations, dependency versions (via `dependencyManagement`), and build settings for all sub-modules.
    *   Modules: `twelvet-framework`, `twelvet-api`.

*   **`twelvet-framework`:**
    *   Provides common functionalities and utilities shared across different microservices.
    *   Sub-modules include:
        *   `twelvet-framework-core`: Basic utilities, constants, and core abstractions.
        *   `twelvet-framework-datascope`: Data permission control.
        *   `twelvet-framework-datasource`: Dynamic datasource configuration and management.
        *   `twelvet-framework-jdbc`: JDBC related utilities.
        *   `twelvet-framework-job`: Common components for scheduled jobs (integrates with XXL-Job).
        *   `twelvet-framework-log`: Centralized logging configurations and utilities.
        *   `twelvet-framework-openfeign`: Configuration for Feign (declarative REST client).
        *   `twelvet-framework-redis`: Redis client configuration and utilities.
        *   `twelvet-framework-security`: Security configurations, likely using Spring Security.
        *   `twelvet-framework-swagger`: Swagger/OpenAPI documentation setup.
        *   `twelvet-framework-utils`: General utility classes.

*   **`twelvet-api`:**
    *   Defines the API interfaces (DTOs, Feign client interfaces) for inter-service communication. This ensures loose coupling between services.
    *   Sub-modules for each service domain:
        *   `twelvet-api-ai`
        *   `twelvet-api-auth`
        *   `twelvet-api-dfs`
        *   `twelvet-api-gen`
        *   `twelvet-api-job`
        *   `twelvet-api-system`

*   **`twelvet-auth`:**
    *   Handles authentication and authorization logic. It likely acts as the OAuth2 authorization server or integrates with one.

*   **`twelvet-gateway`:**
    *   The API Gateway for the system, built using Spring Cloud Gateway.
    *   Responsible for request routing, filtering, rate limiting, and other cross-cutting concerns at the entry point of the system.

*   **`twelvet-nacos`:**
    *   This module might contain specific configurations related to Nacos client setup or custom Nacos interactions. Often, Nacos is run as a separate server.

*   **`twelvet-server`:**
    *   Contains the business logic and implementation for various microservices. Each sub-module typically corresponds to a specific domain.
    *   Sub-modules:
        *   `twelvet-server-ai`: Implements AI-related functionalities.
        *   `twelvet-server-dfs`: Implements Distributed File System services.
        *   `twelvet-server-gen`: Implements code generation tools and services.
        *   `twelvet-server-job`: Implements services related to task scheduling.
        *   `twelvet-server-system`: Implements core system functionalities (e.g., user management, roles, permissions).

*   **`twelvet-visual`:**
    *   Modules dedicated to monitoring and visualization.
    *   Sub-modules:
        *   `twelvet-visual-monitor`: Integrates with Spring Boot Admin server for application monitoring.
        *   `twelvet-visual-sentinel`: Likely hosts the Sentinel dashboard or provides Sentinel-specific configurations for monitoring and controlling service traffic.

*   **`docker/` Directory:**
    *   Contains Dockerfiles and deployment scripts (`docker-compose.yml`, `deploy.sh`) for building and running the various microservices as Docker containers. This indicates a strong focus on containerized deployment.

This modular design allows for independent development, deployment, and scaling of each microservice, which is a hallmark of a robust microservices architecture.
