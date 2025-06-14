--
-- PostgreSQL database dump
--

-- Dumped from database version 13.20
-- Dumped by pg_dump version 13.20

-- Started on 2025-06-11 21:06:11

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 3039 (class 0 OID 24742)
-- Dependencies: 209
-- Data for Name: commande; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.commande (id_commande, nom_client, quantite_voulue, date_commande, id) FROM stdin;
19	DJAMSO	\N	2025-06-11	\N
20	AWRWTFR	\N	2025-06-11	\N
\.


--
-- TOC entry 3032 (class 0 OID 16410)
-- Dependencies: 202
-- Data for Name: contient_commande_produit; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.contient_commande_produit (quantite_voulue, prix_unitaire, id_commande, id_produit) FROM stdin;
\.


--
-- TOC entry 3033 (class 0 OID 16413)
-- Dependencies: 203
-- Data for Name: facture; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.facture (montant_total, date_facture, id_facture, id_commande) FROM stdin;
\.


--
-- TOC entry 3030 (class 0 OID 16395)
-- Dependencies: 200
-- Data for Name: produit; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.produit (nom, categorie, prix_unitaire, id_produit, quantite_en_stock) FROM stdin;
PARACETAMOL	AAAA	1500.00	19	5
\.


--
-- TOC entry 3031 (class 0 OID 16398)
-- Dependencies: 201
-- Data for Name: stock; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.stock (quantite, seuil_alerte, id_stock, id_produit) FROM stdin;
\.


--
-- TOC entry 3034 (class 0 OID 24618)
-- Dependencies: 204
-- Data for Name: utilisateurs; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.utilisateurs (nom, identifiant, motdepasse, id) FROM stdin;
zongo	steph	root	1
\.


--
-- TOC entry 3052 (class 0 OID 0)
-- Dependencies: 206
-- Name: Produit_id_produit_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."Produit_id_produit_seq"', 19, true);


--
-- TOC entry 3053 (class 0 OID 0)
-- Dependencies: 205
-- Name: Stock_id_stock_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."Stock_id_stock_seq"', 5, true);


--
-- TOC entry 3054 (class 0 OID 0)
-- Dependencies: 210
-- Name: commande_id_commande_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.commande_id_commande_seq', 23, true);


--
-- TOC entry 3055 (class 0 OID 0)
-- Dependencies: 207
-- Name: facture_id_facture_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.facture_id_facture_seq', 1, true);


--
-- TOC entry 3056 (class 0 OID 0)
-- Dependencies: 208
-- Name: utilisateurs_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.utilisateurs_id_seq', 1, true);


-- Completed on 2025-06-11 21:06:12

--
-- PostgreSQL database dump complete
--

