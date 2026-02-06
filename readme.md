# CoolBank - Core Banking System

![Java](https://img.shields.io/badge/Java-25-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4-green.svg)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue.svg)
![Security](https://img.shields.io/badge/Spring%20Security-JWT-red.svg)
![Paystack](https://img.shields.io/badge/Integration-Paystack-blueviolet.svg)

**CoolBank** is a fintech backend built with Java and Spring Boot. It simulates a complete financial lifecycle, allowing users to create wallets, fund them via payment gateways(Paystack), perform atomic P2P transfers, and withdraw funds.

It features an **Admin Dashboard** for fraud detection, user management, and audit logging, built with a secure, event-driven architecture.

---

## Key Features

###  **User Module**
* **Secure Authentication:** Registration, Login, and Session management using **JWT (Access + Refresh Tokens)**.
* **Event-Driven Notifications:** Asynchronous email verification and password resets using Spring Events (`ApplicationEventPublisher`).
* **Wallet System:** Automatic NGN wallet creation upon registration.
* **Transaction PIN:** Secure 4-digit PIN required for all outgoing transactions (hashed for security).
* **Funding:** Real-time wallet funding integration with **Paystack API**.
* **Transfers:** Atomic P2P transfers between users (ACID compliant).
* **Withdrawals:** Mock withdrawal system simulating external bank transfers with balance validation.

###  **Admin Module**
* **Dashboard Analytics:** Real-time statistics on Total Volume, Active Users, and Pending KYC.
* **User Management:**
    * **Freeze/Unfreeze** suspicious accounts.
    * **KYC Upgrades** (Tier 1 → Tier 3).
    * **360° Profile View:** See user details, balance, and history.
* **Treasury Management:**
    * **Manual Credit/Debit:** Admin can manually adjust balances (e.g., for refunds or penalties).
    * **System Reconciliation:** View global transaction history.
* **Audit Logs:** Immutable logs tracking *who* performed an admin action and *why*.

---

##  Tech Stack

| Component | Technology                     |
| :--- |:-------------------------------|
| **Language** | Java 25                        |
| **Framework** | Spring Boot 4                  |
| **Database** | PostgreSQL                     |
| **Security** | Spring Security 6, JWT, BCrypt |
| **Documentation** | Swagger UI (OpenAPI 3.0)       |
| **Payment Gateway** | Paystack                       |
| **Messaging** | Java Mail Sender (SMTP)        |
| **Build Tool** | Maven                          |

---

## Setup & Installation

### 1. Prerequisites
* Java 25 Development Kit (JDK)
* PostgreSQL installed and running
* Maven

### 2. Clone the Repository
```bash
git clone [https://github.com/olalekan67/cool-bank.git](https://github.com/olalekan67/cool-bank.git)
cd coolbank