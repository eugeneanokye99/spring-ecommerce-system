# GitHub Secrets Setup Guide

This document describes the GitHub Secrets required for the CI/CD pipeline to function correctly.

## Required Secrets

### TEST_DB_PASSWORD

**Purpose**: Password for the PostgreSQL test database used in CI/CD pipelines.

**How to set up**:

1. Navigate to your repository on GitHub
2. Go to **Settings** → **Secrets and variables** → **Actions**
3. Click **New repository secret**
4. Enter the following details:
   - **Name**: `TEST_DB_PASSWORD`
   - **Value**: Your test database password (e.g., `Final@2025`)
5. Click **Add secret**

## Security Best Practices

- Never commit sensitive credentials directly in workflow files or source code
- Use GitHub Secrets for all sensitive values (passwords, API keys, tokens)
- Rotate secrets regularly
- Use different credentials for test/staging/production environments
- Limit secret access to only the workflows that need them

## Related Files

- `.github/workflows/ci.yml` - CI/CD pipeline that uses these secrets
