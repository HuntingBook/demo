要在 GitHub 上 **运行这个部署脚本**（GitHub Actions），你只需要按照以下步骤操作：

---

## ✅ 步骤一：放置脚本文件到正确位置

将你刚才写好的部署脚本（`.yml` 文件）保存到你项目的 Git 仓库中，路径应为：

```
.github/workflows/deploy.yml
```

确保目录结构如下：

```
your-repo/
├── .github/
│   └── workflows/
│       └── deploy.yml   ✅ 你的部署脚本
├── flight-client/
├── flight-api/
├── ...
```

---

## ✅ 步骤二：Push 到 GitHub 仓库

确保你已经提交 `.github/workflows/deploy.yml` 并推送到远程仓库的 `main` 分支：

```bash
git add .github/workflows/deploy.yml
git commit -m "Add ECS deploy workflow"
git push origin main
```

---

## ✅ 步骤三：配置 Secrets

在 GitHub 仓库的页面中，依次进入：

**Settings → Secrets and variables → Actions → Repository secrets**

添加以下必要的 secrets（名称要匹配脚本）：

| Secret Name             | 示例值                            |
| ----------------------- | ------------------------------ |
| `AWS_ACCESS_KEY_ID`     | `AKIAXXXXXXXXXXXXXXX`          |
| `AWS_SECRET_ACCESS_KEY` | `wJalrXUtnFEMI/K7MDENG/bPxRfi` |
| `AWS_REGION`            | `ap-southeast-2`               |
| `AWS_ACCOUNT_ID`        | `123456789012`                 |
| `ECR_REPOSITORY`        | `your-ecr-repo-name`           |
| `ECS_CLUSTER_NAME`      | `your-ecs-cluster-name`        |
| `ECS_SERVICE_NAME`      | `your-ecs-service-name`        |

---

## ✅ 步骤四：推送代码触发部署

你的 `.yml` 文件中有：

```yaml
on:
  push:
    branches:
      - main
```

说明只要你往 `main` 分支推送代码，GitHub Actions 就会自动执行这个脚本部署到 AWS ECS：

```bash
git add .
git commit -m "Trigger ECS deployment"
git push origin main
```

---

## ✅ 步骤五：查看运行日志

进入你的 GitHub 仓库 → 点击 “**Actions**” → 找到最新的工作流运行记录，点击进去可以看到详细步骤、日志、是否成功等。

---

如果你还没有设置 ECR 和 ECS，我也可以帮你一步步初始化。需要吗？
