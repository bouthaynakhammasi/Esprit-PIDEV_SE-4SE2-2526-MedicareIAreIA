<div align="center">

# 🏥 MedicareIA — Frontend

**Plateforme intelligente de gestion médicale**  
*Projet académique PIDEV · Esprit School of Engineering*

<br>

[![Angular](https://img.shields.io/badge/Angular-17+-dd0031.svg?style=for-the-badge&logo=angular&logoColor=white)](https://angular.io/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.x-007ACC?style=for-the-badge&logo=typescript&logoColor=white)](https://www.typescriptlang.org/)
[![Bootstrap](https://img.shields.io/badge/Bootstrap-5.x-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white)](https://getbootstrap.com/)
[![Node](https://img.shields.io/badge/Node.js-18+-339933?style=for-the-badge&logo=nodedotjs&logoColor=white)](https://nodejs.org/)

<br>
</div>

---

## 📌 À propos

**MedicareIA** est une solution complète de gestion médicale intégrant l'Intelligence Artificielle, développée dans le cadre du projet PIDEV à l'Esprit.

Ce repository contient le code source du **frontend Angular**, qui constitue l'interface utilisateur de la plateforme. Il communique avec deux backends distincts : un backend Java Spring Boot pour la logique métier et un service FastAPI Python pour les fonctionnalités IA.

---

## ✨ Fonctionnalités

| Module | Description |
|:---:|---|
| 👶 **Baby Care** | Suivi personnalisé des soins, calendrier et tableau de bord parent. |
| 🩺 **Espace Docteur** | Gestion des disponibilités et calendrier des rendez-vous. |
| 💬 **Messagerie** | Communication en temps réel via WebSockets. |
| 🤖 **IA Médicale** | Diagnostic assisté par IA (analyse de peau, recommandations). |
| 🔐 **Authentification**| Gestion des rôles (Patient, Docteur, Admin). |

---

## 🧰 Prérequis

Avant de commencer, assurez-vous d'avoir installé les éléments suivants :

* <img src="https://upload.wikimedia.org/wikipedia/commons/d/d9/Node.js_logo.svg" width="20"/> **Node.js** ≥ 18.x → [Télécharger](https://nodejs.org/)
* <img src="https://angular.io/assets/images/logos/angular/angular.svg" width="20"/> **Angular CLI** 17.x

```bash
npm install -g @angular/cli@17
```

---

## 🚀 Installation rapide

**1. Cloner le repository et se placer sur la branche frontend**

```bash
git clone https://github.com/bouthaynakhammasi/Esprit-PIDEV_SE-4SE2-2526-MedicareIAreIA.git
cd Esprit-PIDEV_SE-4SE2-2526-MedicareIAreIA
git checkout frontend
cd frontend/medicarepi
```

**2. Installer les dépendances**

```bash
npm install
```

**3. Lancer en développement**

```bash
ng serve
```

> 💡 L'application est accessible sur **http://localhost:4200** et se recharge automatiquement à chaque modification du code.

---

## 🏗️ Architecture du projet

<details>
<summary><b>Cliquez pour dérouler l'arborescence</b></summary>

```text
src/
├── app/
│   ├── features/           # Modules fonctionnels
│   │   ├── front-office/   # Interface publique
│   │   ├── back-office/    # Interface administrateur
│   │   ├── patient/        # Espace patient (Baby Care)
│   │   └── docteur/        # Espace médecin
│   ├── services/           # Services HTTP (Spring Boot + FastAPI)
│   ├── shared/             # Composants & modules réutilisables
│   └── models/             # Interfaces et types TypeScript
├── assets/                 # Ressources statiques
└── environments/           # Configuration par environnement
```
</details>

---

## 📡 Connexion aux backends

Ce frontend interagit de manière transparente avec deux services backend :

| Backend | Technologie | Port | Rôle |
|:---|:---|:---:|:---|
| ⚙️ **API Métier** | Spring Boot 3 / Java 17 | `8080` | CRUD, sécurité, gestion des données |
| 🧠 **API IA** | FastAPI / Python | `8000` | Diagnostic IA, analyse d'image |

> ⚠️ **Attention :** Les deux backends doivent être lancés localement en parallèle pour bénéficier de l'intégralité des fonctionnalités de la plateforme.

---

## ⚙️ Scripts disponibles

| Commande | Action |
|---|---|
| `ng serve` | Lance le serveur local de développement. |
| `ng build` | Compile le projet dans le dossier `dist/`. |
| `ng build --configuration production` | Génère un build optimisé pour la production. |
| `ng test` | Lance les tests unitaires. |
| `ng lint` | Analyse statique du code source. |

---

<div align="center">
  <h3>👥 Équipe</h3>
  <p>Développé avec ❤️ par l'équipe <b>SE-4SE2-2526</b> dans le cadre du module PIDEV à l'<b>Esprit School of Engineering</b>.</p>
  <br>
  <i>MedicareIA · PIDEV 2025-2026 · Esprit</i>
</div>
