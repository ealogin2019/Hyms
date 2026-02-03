🧭 HYMS PRODUCT & ENGINEERING ROADMAP (v1 → v3)
PHASE 0 — FOUNDATION (✅ DONE)

Status: Completed / Stable

Architecture

Modular structure (core / feature / domain separation)

Hilt DI wired correctly

Room + Flow + persistence proven

Draft autosave survives process death

Recent views rail working

Feed grid clickable + stable

Photo Picker integrated (local URIs)

UX baseline

Minimal feed

Image-led layout

Clean navigation

No clutter

UK defaults (GBP, sizing hints)

This phase proves the core loop works.

PHASE 1 — CORE MARKETPLACE LOOP (🚧 CURRENT / NEXT)
1.1 Seller Flow (in progress)

✅ Draft basics

✅ Draft photos (local)

⏭ Remove photo (long-press / subtle ✕)

⏭ Photo reorder (drag later)

⏭ Publish button (no backend yet)

Goal: Seller can confidently create a listing without friction.

1.2 Listing Detail Screen (NEXT – HIGH PRIORITY)

This is the heart of the buyer experience.

Must be:

Image-first

Minimal metadata

One primary CTA (Buy / Message)

Secondary actions hidden (save, report)

Includes:

Image carousel

Price (GBP)

Title

Size / condition

Seller preview (avatar + rating placeholder)

No reviews, no shipping yet.

1.3 Buyer Actions (NEXT)

Save / favorite listing

Message seller (UI only)

Recently viewed continues silently

Still no backend dependency.

1.4 Search & Filters (Minimal)

Keyword search

Category

Price range

Size

Condition

Rule: filters are hidden by default → bottom sheet.

PHASE 2 — TRUST & TRANSACTIONS (v2)
2.1 Photo Upload + Backend Sync

Upload photos immediately after pick

Replace local URIs with remote URLs

Retry queue

Progress indicators (subtle)

This solves all URI persistence issues permanently.

2.2 Chat (Transactional)

Buyer ↔ Seller

Item-linked threads

System prompts (“Item sold”, “Offer accepted”)

Minimal, calm, WhatsApp-like—not noisy.

2.3 Offers & Negotiation

Offer price

Accept / Counter / Decline

No bidding chaos

2.4 Trust Signals (Minimal)

Seller rating placeholder

Account age

Verified badge (future)

No gamification.

PHASE 3 — PAYMENTS, SHIPPING, SCALE (v3)
3.1 Checkout

Stripe / Adyen

Escrow-like flow

Buyer protection copy (short, calm)

3.2 Shipping

UK shipping presets

Label generation

Tracking status

3.3 Moderation & Safety

Report item

Auto-flagging

Admin tooling (internal)

3.4 Performance & Growth

Caching

Pagination tuning

A/B experiments (carefully)

🧠 CROSS-PHASE NON-NEGOTIABLES (Always enforced)

✔ Fluid transitions
✔ Minimal UI
✔ One primary action per screen
✔ Progressive disclosure
✔ Image > text
✔ Seller & buyer never confused
✔ No feature creep
✔ No “just in case” UI
✔ Calm, aesthetic, precise

If a feature violates this → it does not ship.