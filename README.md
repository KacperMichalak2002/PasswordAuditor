Password Auditor
- **Entropy Calculation:** Calculates information entropy in bits.
- **Rule-based Checks:** Validates length, character types (uppercase, lowercase, digits, symbols).
- **Common Password Detection:** Checks against a local database of top 10,000 most common passwords.

- **Brute Force Attack:** Multithreaded simulation (`Task`) to demonstrate how long it takes to crack a password character by character.
- **Dictionary Attack:** Simulates an attack using multiple dictionary files (e.g., RockYou subset, Polish common passwords).

- **Have I Been Pwned Integration:** Uses the **HIBP API** with *k-Anonymity* (SHA-1 prefix search) to safely check if the password has appeared in known data breaches without sending the full password over the network.

## Tech Stack
- **Language:** Java 21
- **Framework:** JavaFX
- **Build Tool:** Maven
- **Styling:** CSS
